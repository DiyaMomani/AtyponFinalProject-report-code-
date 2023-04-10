package com.example.worker.Services;

import com.example.worker.Affinity.Affinity;
import com.example.worker.ApiResponse;
import com.example.worker.DB.DB_Handler;
import com.example.worker.Indexing.DatabaseIndex;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class QueryService {
    private final DB_Handler dbHandler;

    @Autowired
    public QueryService(DB_Handler dbHandler){
        this.dbHandler = dbHandler;
    }

    public ApiResponse deleteById(String database, String Collection, String id){
        ApiResponse response = dbHandler.deleteWithId(database, Collection, id+"");

        DatabaseIndex index = DatabaseIndex.getInstance();
        HashMap<String, List<Integer>> map1 = index.getMap(database, Collection, "Id");
        int idx = map1.get(id+"").get(0);
        index.deleteByIdx(database,Collection,idx);
        return response;
    }
    public String update(String Database,String Collection ,String id, String Property, String value){
        ObjectNode obj = dbHandler.getById(Database,Collection, id+"");
        String response =  dbHandler.Update(Database,Collection,id+"",Property,value);

        if(!response.equalsIgnoreCase("Update done"))
            return response;


        DatabaseIndex index = DatabaseIndex.getInstance();
        HashMap<String, List<Integer>> map1 = index.getMap(Database,Collection,"Id");
        int idx = map1.get(id+"").get(0);
        HashMap<String, List<Integer>> map = index.getMap(Database,Collection,Property);
        String oldValue = obj.get(Property).asText();
        map.get(oldValue).remove(idx);
        index.addToValue(Database,Collection,Property,value,idx);

        return response;
    }
    @Async
    public Future<ObjectNode> getById(String Database, String Collection, String id){
        return CompletableFuture.completedFuture(dbHandler.getById(Database, Collection, id+""));
    }
    @Async
    public Future<List<ObjectNode>> getByProperty(String Database,String Collection ,String Property, String value){
        DatabaseIndex index = DatabaseIndex.getInstance();
        HashMap<String, List<Integer>> map = index.getMap(Database,Collection,Property);
        List <Integer> indexes = map.get(value);
        if(indexes == null)
            return null;

        List<ObjectNode> ans = new ArrayList<>();
        for(int idx : indexes){
            ans.add(dbHandler.objectAt(Database,Collection,idx));
        }
        return CompletableFuture.completedFuture(ans);
    }
    @Async
    public Future< List<String> > getCollections(String Database){
        DatabaseIndex index = DatabaseIndex.getInstance();
        return CompletableFuture.completedFuture( index.getCollections(Database) );
    }
    @Async
    public Future< List<String> > getDatabases(){
        DatabaseIndex index = DatabaseIndex.getInstance();
        return CompletableFuture.completedFuture( index.getDatabases() );
    }
    @SneakyThrows
    public ApiResponse deleteAllOfValue(String Database, String Collection, String Property, String value){
        DatabaseIndex index = DatabaseIndex.getInstance();
        HashMap<String, List<Integer>> map = index.getMap(Database,Collection,Property);
        if(!map.containsKey(value)) {
            List<Integer> list = map.get(value);

            for(int X : list){
                ObjectNode obj = dbHandler.objectAt(Database,Collection,X);
                String id = obj.get("Id").asText();
                deleteById(Database,Collection,id);
            }
        }
        String path = "./src/main/resources/" + Database + "/" + Collection + ".json";
        File file = new File(path);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = (ObjectNode) objectMapper.readTree(file);
        ArrayNode data = (ArrayNode) root.get("myArray");
        for(int i = 0 ; i < data.size() ; ){
            System.out.println("prop -> " + data.get(i).get(Property).toString());
            if(data.get(i).get(Property).toString().equals("\"" + value + "\"")){
                data.remove(i);
            }
            else
                i++;
        }
        FileWriter writer = new FileWriter(path);
        objectMapper.writeValue(writer, root);
        writer.close();
        Affinity.getInstance().updateAffinity();
        return new ApiResponse("deleting all " +Property +':' + value +" done",200);
    }
    public boolean checkUpdate(String database,String collection,String id,String property,String oldValue) {
        try {
            ObjectNode objectNode = getById(database, collection, id).get();
            String currValue = objectNode.get(property).asText();
            return oldValue.equals(currValue);

        } catch (Exception e) {
            return false;
        }
    }
    @Async
    public Future<ArrayNode> getAll(String Database, String Collection){
        Future empty = CompletableFuture.completedFuture(new ArrayList<>());
        ArrayNode myArray = dbHandler.getAllFrom(Database,Collection);
        if(myArray == null)
            return empty;

        return CompletableFuture.completedFuture(myArray);
    }
}
