package com.example.worker.Services;

import com.example.worker.DB.DAO;
import com.example.worker.DB.schemaCheck;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class CheckerService {
    private final DAO dao;
    @Autowired
    public CheckerService(DAO dao){
        this.dao = dao;
    }

    public boolean checkDB(String database){
        return dao.checkDatabase(database);
    }
    public boolean checkCollection(String database,String collection){
        return dao.CheckCollection(database,collection);
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
        return dao.getById(Database,Collection,id) == null;
    }
    public boolean checkById(String Database, String Collection,String Id){
        return dao.getById(Database,Collection,Id) == null;
    }
}
