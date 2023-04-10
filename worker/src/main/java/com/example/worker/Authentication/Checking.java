package com.example.worker.Authentication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Checking {

    @SneakyThrows
    public boolean trustedUser(String username , String token){
        String path = "src/main/java/com/example/worker/Authentication/User.json";
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);
        ObjectNode root = mapper.readValue(file, ObjectNode.class);
        ArrayNode users = (ArrayNode) root.get("User");
        Map<String, String> jsonMap = new HashMap<>();
        for(JsonNode node : users) {
            jsonMap = mapper.convertValue(
                    mapper.readTree(node.toString()),
                    new TypeReference<>() {
                    }
            );
            System.out.println(jsonMap.get("userName"));
            if(jsonMap.get("userName").equals(username) &&
                    jsonMap.get("token").equals(token))
                return true;
        }
        return false;
    }

    @SneakyThrows
    public boolean trustedAdmin(String username , String token){
        String path = "src/main/java/com/example/worker/Authentication/Admin.json";
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);
        ObjectNode root = mapper.readValue(file, ObjectNode.class);
        ArrayNode users = (ArrayNode) root.get("Admin");
        Map<String, String> jsonMap = new HashMap<>();
        for(JsonNode node : users) {
            jsonMap = mapper.convertValue(
                    mapper.readTree(node.toString()),
                    new TypeReference<>() {
                    }
            );
            System.out.println(jsonMap.get("userName"));
            if(jsonMap.get("userName").equals(username) &&
                    jsonMap.get("token").equals(token))
                return true;
        }
        return false;
    }
}
