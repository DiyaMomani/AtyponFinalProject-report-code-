package com.example.demo.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Service
public class AuthenticationService {
    @SneakyThrows
    public boolean checkUsername(String username){
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
            System.out.println(jsonMap.get("username"));
            if(jsonMap.get("username").equals(username))
                return true;
        }
        return false;
    }

    public boolean checkAdmin(String username , String token){
        return requestAuth("0",username,token,"admin");
    }

    @SneakyThrows
    public boolean checkTokenUser(String username , String token){
        String path = "./src/main/resources/user.json";
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);
        ObjectNode root = mapper.readValue(file, ObjectNode.class);
        ArrayNode users = (ArrayNode) root.get("user");
        String workerId = "0";
        Map<String, String> jsonMap ;
        for(JsonNode node : users) {
            jsonMap = mapper.convertValue(
                    mapper.readTree(node.toString()),
                    new TypeReference<>() {
                    }
            );
            System.out.println(jsonMap.get("username"));
            if(jsonMap.get("username").equals(username)) {
                workerId = jsonMap.get("worker");
                break;
            }
        }
        System.out.println(workerId);
        return requestAuth(workerId,username,token,"user");
    }

    @SneakyThrows
    private boolean requestAuth(String workerId , String name , String token , String type){
        URL dist =new URL("http://localhost:808"+workerId+"/check/" + type);
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("USERNAME" , name);
        conn.setRequestProperty("TOKEN" , token);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
        return response.toString().equals("yes");
    }

    @SneakyThrows
    public String requestToken(String username,String type){
        URL dist =new URL("http://localhost:6060/boot/add/" + type);
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("USERNAME" , username);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return  response.toString();
    }

}
