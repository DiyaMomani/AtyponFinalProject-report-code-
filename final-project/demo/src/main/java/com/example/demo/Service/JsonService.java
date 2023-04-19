package com.example.demo.Service;

import com.example.demo.Model.UserWorker;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

@Service
public class JsonService {
    @SneakyThrows
    public String getWorker(String username){
        String path = "./src/main/resources/user.json";
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);
        ObjectNode root = mapper.readValue(file, ObjectNode.class);
        ArrayNode users = (ArrayNode) root.get("user");
        Map<String, String> jsonMap ;
        for(JsonNode node : users) {
            jsonMap = mapper.convertValue(
                    mapper.readTree(node.toString()),
                    new TypeReference<>() {
                    }
            );
            if(jsonMap.get("username").equals(username))
                return jsonMap.get("worker");
        }
        return "Not Found";
    }
    @SneakyThrows
    public void addUser(UserWorker user){
        String path = "./src/main/resources/user.json";
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);
        ObjectNode root = mapper.readValue(file, ObjectNode.class);
        ArrayNode users = (ArrayNode) root.get("user");
        String json = mapper.writeValueAsString(user);
        JsonNode jsonNode = mapper.readTree(json);
        users.add(jsonNode);
        FileWriter writer = new FileWriter(path);
        mapper.writeValue(writer, root);
        writer.close();
    }
}
