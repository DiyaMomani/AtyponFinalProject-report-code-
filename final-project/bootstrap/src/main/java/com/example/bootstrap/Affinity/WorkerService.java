package com.example.bootstrap.Affinity;

import com.example.bootstrap.model.Admin;
import com.example.bootstrap.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class WorkerService {

    public void addUser(String username , String token){
        User user = setWorker(username,token);
        addJsonUser(user);
        saveIntoWorker(username,token);
        sendToWeb(username);
    }

    public void addAdmin(String username , String token){
        Admin admin = new Admin(username,token);
        addJsonAdmin(admin);
        saveIntoAllWorker(username,token);
    }
    @SneakyThrows
    private void saveIntoWorker(String username , String token){
        String port = "worker" + Worker.getInstance().getWorker().toString();
        String url = "http://"+port+":8080";
        URL dist =new URL(url + "/add/user");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("USERNAME" , username);
        conn.setRequestProperty("TOKEN" , token);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        in.close();

    }
    @SneakyThrows
    public void sendToWeb(String username) {
        URL dist =new URL("http://web:8084/add/user");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("USERNAME" , username);
        conn.setRequestProperty("WORKER" , Worker.getInstance().getWorker().toString());
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        in.close();
        Worker.getInstance().updateWorker();
    }

    @SneakyThrows
    private void saveIntoAllWorker(String username , String token){
        String []urls = {"http://worker0:8080" , "http://worker1:8080" , "http://worker2:8080" , "http://worker3:8080"};
        for(String url : urls) {
            URL dist = new URL(url + "/add/admin");
            HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("USERNAME", username);
            conn.setRequestProperty("TOKEN", token);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            in.close();
        }
    }

    @SneakyThrows
    private void addJsonAdmin(Admin admin){
        String path = "./src/main/resources/admins.json";
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);
        ObjectNode root = mapper.readValue(file, ObjectNode.class);
        ArrayNode users = (ArrayNode) root.get("admins");
        String json = mapper.writeValueAsString(admin);
        JsonNode jsonNode = mapper.readTree(json);
        users.add(jsonNode);
        FileWriter writer = new FileWriter(path);
        mapper.writeValue(writer, root);
        writer.close();
    }

    @SneakyThrows
    private void addJsonUser(User user){
        String path = "./src/main/resources/users.json";
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);
        ObjectNode root = mapper.readValue(file, ObjectNode.class);
        ArrayNode users = (ArrayNode) root.get("users");
        String json = mapper.writeValueAsString(user);
        JsonNode jsonNode = mapper.readTree(json);
        users.add(jsonNode);
        FileWriter writer = new FileWriter(path);
        mapper.writeValue(writer, root);
        writer.close();
    }

    private User setWorker(String username , String token){
        String workerId = Worker.getInstance().getWorker().toString();
        User user = new User(username,token,workerId);
        return user;
    }
}
