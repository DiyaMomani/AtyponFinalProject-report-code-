package com.example.worker.Services;

import com.example.worker.Affinity.Affinity;
import com.example.worker.DB.DAO;
import com.example.worker.Indexing.DbIndex;
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
    private final DAO dao;

    @Autowired
    public QueryService(DAO dao){
        this.dao = dao;
    }

    public String deleteById(String database, String Collection, String id){
        String response = dao.deleteWithId(database, Collection, id+"");

        DbIndex index = DbIndex.getInstance();
        HashMap<String, List<Integer>> map1 = index.getMap(database, Collection, "Id");
        int idx = map1.get(id+"").get(0);
        index.deleteByIndex(database,Collection,idx);
        return response;
    }
    public String update(String Database,String Collection ,String id, String Property, String value){
        ObjectNode obj = dao.getById(Database,Collection, id+"");
        String response =  dao.Update(Database,Collection,id+"",Property,value);

        if(!response.equalsIgnoreCase("Update done"))
            return response;


        DbIndex index = DbIndex.getInstance();
        HashMap<String, List<Integer>> map1 = index.getMap(Database,Collection,"Id");
        int idx = map1.get(id+"").get(0);
        HashMap<String, List<Integer>> map = index.getMap(Database,Collection,Property);
        String oldValue = obj.get(Property).asText();
        map.get(oldValue).remove(idx);
        index.addIndex(Database,Collection,Property,value,idx);

        return response;
    }
    @Async
    public Future<ObjectNode> getById(String Database, String Collection, String id){
        return CompletableFuture.completedFuture(dao.getById(Database, Collection, id+""));
    }
    @Async
    public Future<List<ObjectNode>> getByProperty(String Database,String Collection ,String Property, String value){
        DbIndex index = DbIndex.getInstance();
        HashMap<String, List<Integer>> map = index.getMap(Database,Collection,Property);
        List <Integer> indexes = map.get(value);
        if(indexes == null)
            return null;

        List<ObjectNode> ans = new ArrayList<>();
        for(int idx : indexes){
            ans.add(dao.objectAt(Database,Collection,idx));
        }
        return CompletableFuture.completedFuture(ans);
    }
    @Async
    public Future< List<String> > getCollections(String Database){
        DbIndex index = DbIndex.getInstance();
        return CompletableFuture.completedFuture( index.getColl(Database) );
    }
    @Async
    public Future< List<String> > getDatabases(){
        DbIndex index = DbIndex.getInstance();
        return CompletableFuture.completedFuture( index.getDatabase() );
    }
    @SneakyThrows
    public String deleteAllOfValue(String Database, String Collection, String Property, String value){
        DbIndex index = DbIndex.getInstance();
        HashMap<String, List<Integer>> map = index.getMap(Database,Collection,Property);
        if(!map.containsKey(value)) {
            List<Integer> list = map.get(value);

            for(int X : list){
                ObjectNode obj = dao.objectAt(Database,Collection,X);
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
        return "deleting all " +Property +':' + value +" done";
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
    public Future<ArrayNode>getAll(String Database, String Collection){
        ObjectMapper objectMapper=new ObjectMapper();
        Future empty = CompletableFuture.completedFuture(objectMapper.createArrayNode());
        ArrayNode myArray = dao.getAllFrom(Database,Collection);
        if(myArray == null)
            return empty;

        return CompletableFuture.completedFuture(myArray);
    }
}
