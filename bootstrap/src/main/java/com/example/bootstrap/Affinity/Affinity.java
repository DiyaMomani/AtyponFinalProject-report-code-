package com.example.bootstrap.Affinity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;

import java.io.File;

public class Affinity {
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

    @SneakyThrows
    public Integer getValue(){
        File file = new File("src/main/resources/affinity.json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(file);
        return root.get("affinityServer").intValue();
    }
}
