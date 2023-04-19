package com.example.bootstrap.Affinity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;

import java.io.File;

public class Worker {
    private Worker(){}
    private static Worker instance = null;
    public static Worker getInstance(){
        if(instance == null)
            instance = new Worker();
        return instance;
    }
    @SneakyThrows
    public void updateWorker(){
        File file = new File("src/main/resources/Server.json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(file);
        ((ObjectNode) root).put("Server", (root.get("Server").intValue() + 1) % 4);
        mapper.writeValue(file, root);
    }

    @SneakyThrows
    public Integer getWorker(){
        File file = new File("src/main/resources/Server.json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(file);
        return root.get("Server").intValue();
    }
}
