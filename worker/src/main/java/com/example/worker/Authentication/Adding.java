package com.example.worker.Authentication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;

public class Adding {
    @SneakyThrows
    public void addUser(UserForm form){
        String path = "src/main/java/com/example/worker/Authentication/User.json";
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);
        ObjectNode root = mapper.readValue(file, ObjectNode.class);
        ArrayNode users = (ArrayNode) root.get("User");
        String json = mapper.writeValueAsString(form);
        JsonNode jsonNode = mapper.readTree(json);
        users.add(jsonNode);
        FileWriter writer = new FileWriter(path);
        mapper.writeValue(writer, root);
        writer.close();
    }

    @SneakyThrows
    public void addAdmin(UserForm form) {
        String path = "src/main/java/com/example/worker/Authentication/Admin.json";
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);
        ObjectNode root = mapper.readValue(file, ObjectNode.class);
        ArrayNode users = (ArrayNode) root.get("Admin");
        String json = mapper.writeValueAsString(form);
        JsonNode jsonNode = mapper.readTree(json);
        users.add(jsonNode);
        FileWriter writer = new FileWriter(path);
        mapper.writeValue(writer, root);
        writer.close();
    }
}
