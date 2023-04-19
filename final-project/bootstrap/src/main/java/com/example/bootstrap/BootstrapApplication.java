package com.example.bootstrap;

import lombok.SneakyThrows;
import net.minidev.json.JSONObject;
import org.json.JSONArray;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileWriter;

@SpringBootApplication
public class BootstrapApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootstrapApplication.class, args);
        createFiles();
    }

    private static void createFiles() {
        userFile();
        adminFile();
        affinityFile();
    }

    @SneakyThrows
    private static void userFile() {
        String path = "./src/main/resources/users.json";
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("users", jsonArray);
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(jsonObject.toString());
        fileWriter.close();
    }
    @SneakyThrows
    private static void adminFile() {
        String path = "./src/main/resources/admins.json";
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("admins", jsonArray);
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(jsonObject.toString());
        fileWriter.close();
    }
    @SneakyThrows
    private static void affinityFile() {
        String path = "./src/main/resources/Server.json";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Server", "0");
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(jsonObject.toString());
        fileWriter.close();
    }

}
