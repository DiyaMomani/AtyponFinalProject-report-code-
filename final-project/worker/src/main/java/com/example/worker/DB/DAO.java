package com.example.worker.DB;

import com.example.worker.Affinity.Affinity;
import com.example.worker.Indexing.DbIndex;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

@Repository
public class DAO {
    private String DirPath = "src/main/resources/";
    private String getPath(String database, boolean schema){
        if(schema){
            return DirPath + database + "/schemas";
        }else
            return DirPath + database;
    }
    public String createDatabase(String database) {
        File dbDir = new File( getPath(database,false) );

        if (dbDir.exists() && dbDir.isDirectory()) {
            return "Database already exists";
        }

        if (!dbDir.mkdirs()) {
            return "Error creating database";
        } else {
            File schemasDir = new File( getPath(database,true) );
            System.out.println(getPath(database,true));
            schemasDir.mkdirs();
            Affinity.getInstance().updateAffinity();
            return "Database created successfully";
        }
    }

    public String addCollection(String Database, String Collection, String schema) {
        File lock = new File(getPath(Database,false));
        synchronized (lock) {
            try {
                File dbDir = new File(getPath(Database, false));
                if (!dbDir.exists()) {
                    return "Database does not exist";
                }

                File schemaFile = new File(getPath(Database, true) + '/' + Collection + ".json");
                FileWriter writer = new FileWriter(schemaFile);
                writer.write(schema);
                writer.close();

                File jsonFile = new File(getPath(Database, false) + '/' + Collection + ".json");
                createEmptyJsonArray(jsonFile);
                Affinity.getInstance().updateAffinity();
                return "Collection created successfully";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void createEmptyJsonArray(File file){
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = objectMapper.createArrayNode();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.set("myArray", arrayNode);
        try {
            objectMapper.writeValue(file, objectNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public String addFromJson(String database, String Collection, String Json){

        File dbDir = new File(getPath(database,false));
        if (!dbDir.exists()) {
            return "Database does not exist";
        }
        File schemaFile = new File(getPath(database,true) + '/' + Collection + ".json");
        Scanner scanner = new Scanner(schemaFile);

        // Read the contents of the file
        System.out.println("file -> ");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
        }

        // Close the scanner
        scanner.close();
        schemaCheck checker = new schemaCheck(Json,schemaFile);
        File lock = new File(getPath(database,false) + '/' + Collection);
        synchronized (lock) {
            if (checker.checkJson()) {
                System.out.println("check done ");
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String jsonPath = getPath(database, false) + '/' + Collection + ".json";
                    ObjectNode root = (ObjectNode) objectMapper.readTree(new File(jsonPath));
                    ArrayNode myArray = (ArrayNode) root.get("myArray");
                    if (myArray == null) {
                        myArray = objectMapper.createArrayNode();
                        root.set("myArray", myArray);
                    }
                    JsonNode jsonNode = objectMapper.readTree(Json);
                    myArray.add(jsonNode);
                    FileWriter writer = new FileWriter(jsonPath);
                    objectMapper.writeValue(writer, root);
                    writer.close();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Affinity.getInstance().updateAffinity();
                return "record added successfully";
            }
            return "Adding not valid";
        }
    }

    public ObjectNode getById(String Database, String Collection, String id) {
        String jsonPath = getPath(Database,false) + "/" +Collection+ ".json";

        File dbDir = new File(getPath(Database,false));
        if (!dbDir.exists()) {
            return null;
        }

        File lock = new File(getPath(Database,false) + '/' + Collection + ".json");
        synchronized (lock) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode root = null;
            try {
                root = (ObjectNode) objectMapper.readTree(new File(jsonPath));
                ArrayNode myArray = (ArrayNode) root.get("myArray");

                ObjectNode result = null;
                int idx = indexOfId(Database, Collection, id);
                JsonNode jsonNode = myArray.get(idx);
                if (idx != -1)
                    result = jsonNode.deepCopy();

                return result;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean checkDatabase(String database){
        File dbDir = new File( getPath(database,false) );
        return dbDir.isDirectory();
    }
    public boolean CheckCollection(String Database, String Collection){
        File document = new File( getPath(Database,false) + '/' + Collection + ".json");
        File schema = new File(getPath(Database,true) + '/' + Collection + ".json");
        return document.exists() && schema.exists();
    }
    public String Update(String database, String Collection, String id, String Property, String value) {

        String jsonPath = getPath(database,false) + '/' + Collection + ".json";
        File dbDir = new File(getPath(database,false));
        if (!dbDir.exists()) {
            return "Database does not exist";
        }

        ObjectMapper objectMapper = new ObjectMapper();

            ObjectNode root = null;
            try {
                root = objectMapper.readValue(new File(jsonPath), ObjectNode.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ArrayNode myArray = (ArrayNode) root.get("myArray");
            if (myArray == null) {
                return "myArray not found";
            }

            int idx = indexOfId(database, Collection, id);
            JsonNode nodeToUpdate = null;
            if (idx != -1)
                nodeToUpdate = myArray.get(indexOfId(database, Collection, id));


            if (nodeToUpdate == null) {
                return "Id " + id + " not found";
            }
            synchronized (nodeToUpdate) {


                ((ObjectNode) nodeToUpdate).put(Property, value);

                try {
                    objectMapper.writeValue(new File(jsonPath), root);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Affinity.getInstance().updateAffinity();
            }
            return "Update done";

    }

    private int indexOfId(String Database, String Collection, String id){
        DbIndex index = DbIndex.getInstance();
        HashMap<String, List<Integer>> map = index.getMap(Database, Collection, "Id");
        if(!map.containsKey(id) || map.get(id).isEmpty())
            return -1;
        int idx = map.get(id).get(0);
        return idx;
    }


    public String deleteWithId(String database, String Collection, String id) {
        String jsonPath = getPath(database,false) + "/" + Collection + ".json";
        File dbDir = new File(getPath(database,false));
        if(!dbDir.exists()) {
            return "Database does not exist";
        }


        ObjectMapper objectMapper = new ObjectMapper();
        File lock = new File(getPath(database,false) + '/' + Collection + ".json");
        synchronized (lock) {
            if(!lock.exists()) {
                return "Collection does not exist";
            }

            ObjectNode root = null;
            try {
                root = objectMapper.readValue(new File(jsonPath), ObjectNode.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ArrayNode myArray = (ArrayNode) root.get("myArray");
            if (myArray == null) {
                return "myArray not found";
            }

            int idx = indexOfId(database, Collection, id);
            if (idx == -1)
                return "id " + id + " not found";
            myArray.remove(idx);

            try {
                objectMapper.writeValue(new File(jsonPath), root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Affinity.getInstance().updateAffinity();
            return "delete done";
        }
    }
    public int arrSize(String database, String Collection){
        String jsonPath = getPath(database,false) + '/' + Collection + ".json";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode root = null;
            root = (ObjectNode) objectMapper.readTree(new File(jsonPath));
            ArrayNode myArray = (ArrayNode) root.get("myArray");
            return myArray.size();
        } catch (IOException e) {
            return 0;
        }
    }
    public ObjectNode objectAt(String database, String Collection, int idx){
        String jsonPath = getPath(database,false) + '/' + Collection + ".json";
        File dbDir = new File(getPath(database,false));
        if (!dbDir.exists()) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode root = null;
        try {
            root = objectMapper.readValue(new File(jsonPath), ObjectNode.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ArrayNode myArray = (ArrayNode) root.get("myArray");
        if (myArray == null || idx < 0 || idx >= myArray.size()) {
            return null;
        }
        return myArray.get(idx).deepCopy();
    }
    public String dropCollection(String Database, String Collection){
        File dbFile = new File(getPath(Database,false));
        if(!dbFile.exists() || !dbFile.isDirectory())
            return "Database: " + Database + " not exist";
        File lock = new File(getPath(Database,false));
        synchronized (lock) {
            File schemaFile = new File(getPath(Database, true) + "/" + Collection + ".json");
            File jsonFile = new File(getPath(Database, false) + "/" + Collection + ".json");
            if (!schemaFile.exists() || !jsonFile.exists())
                return "Collection: " + Collection + " not exist";
            schemaFile.delete();
            jsonFile.delete();
            return "dropping Collection done";
        }
    }
    @SneakyThrows
    public String dropDatabase(String Database){
        File dbFile = new File(getPath(Database,false));
        if(!dbFile.exists() || !dbFile.isDirectory())
            return "Database " + Database + " not exist";
        Path path = Paths.get(getPath(Database , false));
        Files.walk(path)
                .sorted(java.util.Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);

        Affinity.getInstance().updateAffinity();
        return "dropping Database done";
    }
    
    public ArrayNode getAllFrom(String Database, String Collection) {
        File dbFile = new File(getPath(Database,false));
        if(!dbFile.exists() || !dbFile.isDirectory())
            return null;
        File jsonFile = new File(getPath(Database, false) + "/" + Collection + ".json");
        if (!jsonFile.exists())
            return null;
        String jsonPath = getPath(Database,false) + '/' + Collection + ".json";
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = null;
        try {
            root = (ObjectNode) objectMapper.readTree(new File(jsonPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ArrayNode myArray = (ArrayNode) root.get("myArray");

        return myArray;
    }


}
