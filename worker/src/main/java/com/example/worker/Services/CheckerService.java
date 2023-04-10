package com.example.worker.Services;

import com.example.worker.DB.DB_Handler;
import com.example.worker.DB.schemaCheck;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

@Service
public class CheckerService {
    private final DB_Handler dbHandler;
    @Autowired
    public CheckerService(DB_Handler dbHandler){
        this.dbHandler = dbHandler;
    }

    public boolean checkDB(String database){
        return dbHandler.checkDatabase(database);
    }
    public boolean checkCollection(String database,String collection){
        return dbHandler.CheckCollection(database,collection);
    }

    public boolean checkId(String Database, String Collection,String Json){
        File schemaFile = new File("src/main/resources/" + Database + "/schema/" + Collection + ".json");
        schemaCheck checker = new schemaCheck(Json,schemaFile);
        if(!checker.checkJson())
            return false;

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = null;
        try {
            objectNode = objectMapper.readTree(Json).deepCopy();
        } catch (JsonProcessingException e) {
            return false;
        }
        String id = objectNode.get("Id").asText();
        return dbHandler.getById(Database,Collection,id) == null;
    }
    public boolean checkById(String Database, String Collection,String Id){
        return dbHandler.getById(Database,Collection,Id) == null;
    }
}
