package com.example.worker.Services;

import com.example.worker.ApiResponse;
import com.example.worker.DB.DB_Handler;
import com.example.worker.Indexing.DatabaseIndex;
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
    private final DB_Handler dbHandler;
    @Autowired
    public CreationService(DB_Handler dbHandler){
        this.dbHandler = dbHandler;
    }

    public String initDatabase(String database){
        return dbHandler.createDatabase(database);
    }

    public String initCollection(String Database , String Collection, String schema){
        return dbHandler.addCollection(Database,Collection,schema);
    }

    public String addRecord(String Database, String Collection, String json){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode objectNode = objectMapper.readTree(json).deepCopy();
            String id = objectNode.get("Id").asText();
            DatabaseIndex index = DatabaseIndex.getInstance();
            if( index.isIndexed(Database,Collection) ){
                HashMap<String, List<Integer>> map = index.getMap(Database,Collection,"Id");
                if(map.containsKey(id))
                    return "Id " + id + " is used";
            }

        } catch (JsonProcessingException e) {
            return "Add record faild";
        }

        String response = dbHandler.addFromJson(Database,Collection,json);

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

        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
            list.add(entry.getKey());
        }

        DatabaseIndex index = DatabaseIndex.getInstance();
        if( !index.isIndexed(Database) ){
            index.initIndex(Database,Collection,list.toArray(new String[0]));
        }

        if(!index.isIndexed(Database,Collection)){
            index.addCollection(Database,Collection,list.toArray(new String[0]));
        }

        for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
            index.indexProperties(Database, Collection, entry.getKey(), entry.getValue().toString(), (dbHandler.arrSize(Database,Collection)-1));
        }

        return response;
    }
    public String dropCollection(String Database, String Collection){
        DatabaseIndex index = DatabaseIndex.getInstance();
        index.DropCollection(Database,Collection);
        return dbHandler.dropCollection(Database,Collection);
    }
    public ApiResponse dropDatabase(String Database){
        DatabaseIndex index = DatabaseIndex.getInstance();
        index.DropDatabase(Database);
        return dbHandler.dropDatabase(Database);
    }



}