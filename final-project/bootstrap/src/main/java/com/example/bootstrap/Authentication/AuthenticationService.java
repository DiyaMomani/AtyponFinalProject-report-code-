package com.example.bootstrap.Authentication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
public class AuthenticationService {
    @SneakyThrows
    public boolean isExistUser(String username){
        String path = "./src/main/resources/users.json";
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);
        ObjectNode root = mapper.readValue(file, ObjectNode.class);
        ArrayNode users = (ArrayNode) root.get("users");
        Map<String, String> jsonMap ;
        for(JsonNode node : users) {
            jsonMap = mapper.convertValue(
                    mapper.readTree(node.toString()),
                    new TypeReference<>() {
                    }
            );
            System.out.println(jsonMap.get("username"));
            if(jsonMap.get("username").equals(username))
                return true;
        }
        return false;
    }

    @SneakyThrows
    public boolean isExistAdmin(String username){
        String path = "./src/main/resources/admins.json";
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);
        ObjectNode root = mapper.readValue(file, ObjectNode.class);
        ArrayNode users = (ArrayNode) root.get("admins");
        Map<String, String> jsonMap ;
        for(JsonNode node : users) {
            jsonMap = mapper.convertValue(
                    mapper.readTree(node.toString()),
                    new TypeReference<>() {
                    }
            );
            System.out.println(jsonMap.get("username"));
            if(jsonMap.get("username").equals(username))
                return true;
        }
        return false;
    }
}
