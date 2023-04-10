package com.example.worker.Affinity;

import com.example.worker.ApiResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.File;
@Service
public class Affinity {
    private final AffinityBroadcast broadcaster = new AffinityBroadcast();

    private Affinity(){}
    private static Affinity instance = null;
    public static Affinity getInstance(){
        if(instance == null)
            instance = new Affinity();
        return instance;
    }
    @SneakyThrows
    public void updateAffinity(){
        File file = new File("src/main/resources/affinity.json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(file);
        ((ObjectNode) root).put("affinityServer", (root.get("affinityServer").intValue() + 1) % 4);
        mapper.writeValue(file, root);
    }
    public String BuildDataBase(String dbName){
        String port = "worker" + getValue();
        return broadcaster.buildDatabase(dbName,port);
    }
    public String collectionAffinity(String dbName , String collectionName , String json){
        String port = "worker" + getValue();
        return broadcaster.buildCollection(dbName,collectionName,json,port);
    }
    public String  addRecord(String dbName , String collection , String json){
        String port = "worker" + getValue();
        return broadcaster.addRecord(dbName,collection,json,port);
    }

    public ApiResponse deleteDatabase(String dbName ){
        String port = "worker" + getValue();
        return broadcaster.deleteDatabase(dbName,port);
    }

    public String deleteCollection(String dbName , String collection ){
        String port = "worker" + getValue();
        return broadcaster.deleteCollection(dbName,collection ,port);
    }
    public ApiResponse deleteWithId(String dbName,String collection , String id){
        String port = "worker" + getValue();
        return broadcaster.deleteWithId(dbName,collection,id,port);
    }
    public ApiResponse deleteAllOfValue(String dbName,String collection,String property,String value){
        String port = "worker" + getValue();
        return broadcaster.deleteAllOfValue(dbName,collection,property,value,port);
    }
    public String update(String database,String collection,String id,JsonNode requestBody){
        String port = "worker" + getValue();
        return broadcaster.update(database,collection,id,requestBody,port);
    }

    @SneakyThrows
    public int getValue(){
        File file = new File("src/main/resources/affinity.json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(file);
        return root.get("affinityServer").intValue();
    }
}
