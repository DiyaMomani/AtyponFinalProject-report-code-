package com.example.worker.DB;

import com.example.worker.Affinity.Affinity;
import com.example.worker.ApiResponse;
import com.example.worker.Indexing.DatabaseIndex;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Repository
public class DB_Handler {
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
            return "Database already exists : 400";
        }

        if (!dbDir.mkdirs()) {
            return "Error creating database : 500";
        } else {
            File schemasDir = new File( getPath(database,true) );
            System.out.println(getPath(database,true));
            schemasDir.mkdirs();
            Affinity.getInstance().updateAffinity();
            return "Database created successfully : 200";
        }
    }

    public String addCollection(String Database, String Collection, String schema) {
        try {
            File dbDir = new File(getPath(Database,false));
            if (!dbDir.exists()) {
                return "Database does not exist : 300";
            }

            File schemaFile = new File( getPath(Database,true) + '/' + Collection + ".json");
            FileWriter writer = new FileWriter(schemaFile);
            writer.write(schema);
            writer.close();

            File jsonFile = new File(getPath(Database,false) + '/' + Collection + ".json");
            createEmptyJsonArray(jsonFile);
            Affinity.getInstance().updateAffinity();
            return "JSON saved successfully with file name: " + Database + " : 200";
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    public String addFromJson(String database,String Collection,String Json){
        File dbDir = new File(getPath(database,false));
        if (!dbDir.exists()) {
            return "Database does not exist";
        }
        File schemaFile = new File(getPath(database,true) + '/' + Collection + ".json");

        schemaCheck checker = new schemaCheck(Json,schemaFile);
        if( checker.checkJson() ){
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonPath = getPath(database,false) + '/' + Collection + ".json";
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
            return"record added successfully";
        }
        return "Adding not valid";
    }

    public ObjectNode getById(String Database, String Collection, String id) {
        String jsonPath = getPath(Database,false) + "/" +Collection+ ".json";

        File dbDir = new File(getPath(Database,false));
        if (!dbDir.exists()) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = null;
        try {
            root = (ObjectNode) objectMapper.readTree(new File(jsonPath));
            ArrayNode myArray = (ArrayNode) root.get("myArray");

            ObjectNode result = null;
            int idx = indexOfId(Database, Collection, id);
            JsonNode jsonNode = myArray.get( idx );
            if(idx != -1)
                result = jsonNode.deepCopy();

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
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

        int idx = indexOfId(database,Collection,id);
        JsonNode nodeToUpdate = null;
        if(idx != -1)
            nodeToUpdate = myArray.get( indexOfId(database,Collection,id) );


        if (nodeToUpdate == null) {
            return "Id " + id + " not found";
        }

        ((ObjectNode) nodeToUpdate).put(Property, value);

        try {
            objectMapper.writeValue(new File(jsonPath), root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Affinity.getInstance().updateAffinity();
        return "Update done";
    }

    private int indexOfId(String Database, String Collection, String id){
        DatabaseIndex index = DatabaseIndex.getInstance();
        HashMap<String, List<Integer>> map = index.getMap(Database, Collection, "Id");
        if(!map.containsKey(id) || map.get(id).isEmpty())
            return -1;
        int idx = map.get(id).get(0);
        return idx;
    }


    public ApiResponse deleteWithId(String database, String Collection, String id) {
        String jsonPath = getPath(database,false) + "/" + Collection + ".json";
        File dbDir = new File(getPath(database,false));
        if(!dbDir.exists()) {
            return new ApiResponse("Database does not exist", 500);
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
            return new ApiResponse("myArray not found", 500);
        }

        int idx = indexOfId(database,Collection,id);
        if(idx == -1)
            return new ApiResponse("id " + id + " not found" , 500);
        myArray.remove(idx);

        try {
            objectMapper.writeValue(new File(jsonPath), root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Affinity.getInstance().updateAffinity();
        return new ApiResponse("delete done", 200);
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
            return "Database: " + Database + " not exist : 302";
        File schemaFile = new File(getPath(Database,true) + "/" + Collection + ".json");
        File jsonFile = new File(getPath(Database,false) + "/" + Collection + ".json");
        if(!schemaFile.exists() || !jsonFile.exists())
            return "Collection: " + Collection + " not exist : 302";
        schemaFile.delete();
        jsonFile.delete();
        return "dropping Collection " + Collection + " done : 202";
    }
    public ApiResponse dropDatabase(String Database){
        File dbFile = new File(getPath(Database,false));
        if(!dbFile.exists() || !dbFile.isDirectory())
            return new ApiResponse("Database " + Database + " not exist",302);
        File[] files = dbFile.listFiles();
        if(files != null){
            for(File f : files)
                f.delete();
        }
        dbFile.delete();
        Affinity.getInstance().updateAffinity();
        return new ApiResponse("dropping Database " + Database + " done",202);
    }
    
    public ArrayNode getAllFrom(String Database, String Collection) {
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
