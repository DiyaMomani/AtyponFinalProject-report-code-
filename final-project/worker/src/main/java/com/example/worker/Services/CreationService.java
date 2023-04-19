package com.example.worker.Services;

import com.example.worker.DB.DAO;
import com.example.worker.Indexing.DbIndex;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CreationService {
    private final DAO dao;
    @Autowired
    public CreationService(DAO dao){
        this.dao = dao;
    }

    public String initDatabase(String database){
        return dao.createDatabase(database);
    }

    public String initCollection(String Database , String Collection, String schema){
        return dao.addCollection(Database,Collection,schema);
    }

    public String addRecord(String Database, String Collection, String json){
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("here 1 .");
        try {
            ObjectNode objectNode = objectMapper.readTree(json).deepCopy();
            System.out.println(objectNode.toString());
            String id = objectNode.get("Id").asText();
            System.out.println("id -> " + id);
            DbIndex index = DbIndex.getInstance();
            if( index.containsKey(Database,Collection) ){
                HashMap<String, List<Integer>> map = index.getMap(Database,Collection,"Id");
                if(map.containsKey(id))
                    return "Id " + id + " is used";
            }

        } catch (JsonProcessingException e) {
            return "Add record faild";
        }
        System.out.println("here 2 .");
        String response = dao.addFromJson(Database,Collection,json);
        System.out.println("res -> " + response);
        if(!response.equalsIgnoreCase("record added successfully"))
            return response;

        Map<String, Object> jsonMap = null;
        try {
            jsonMap = objectMapper.convertValue(objectMapper.readTree(json),
                    new TypeReference<>() {
                    });
        } catch (JsonProcessingException e) {
            return "json unable to be read";
        }
        System.out.println("here 3 .");
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
            list.add(entry.getKey());
        }

        DbIndex index = DbIndex.getInstance();
        if( !index.containsKey(Database) ){
            index.createIndex(Database,Collection,list.toArray(new String[0]));
        }

        if(!index.containsKey(Database,Collection)){
            index.addColl(Database,Collection,list.toArray(new String[0]));
        }

        for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
            index.indexProperties(Database, Collection, entry.getKey(), entry.getValue().toString(), (dao.arrSize(Database,Collection)-1));
        }

        return response;
    }
    public String dropCollection(String Database, String Collection){
        DbIndex index = DbIndex.getInstance();
        index.deleteCollection(Database,Collection);
        return dao.dropCollection(Database,Collection);
    }
    public String dropDatabase(String Database){
        DbIndex index = DbIndex.getInstance();
        index.deleteDatabase(Database);
        return dao.dropDatabase(Database);
    }



}