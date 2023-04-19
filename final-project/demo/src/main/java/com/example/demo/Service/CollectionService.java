package com.example.demo.Service;

import com.example.demo.Building.Department;
import com.example.demo.Building.Store;
import com.example.demo.Model.UserToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class CollectionService {
    private JsonService jsonService=new JsonService();
    @SneakyThrows
    public String createCollection(String db_name , String collectionType ,HttpSession session){
        UserToken user = (UserToken) session.getAttribute("entered");
        String workerId = jsonService.getWorker(user.getUsername());
        if(workerId.charAt(0) < '0' || workerId.charAt(0) > '4')
            workerId = "0";
        URL dist =new URL("http://worker"+workerId+":8080/api/create/collection/" + db_name +"/"+collectionType+"/true/true");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("USERNAME" , user.getUsername());
        conn.setRequestProperty("TOKEN" , user.getToken());
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
        ObjectMapper mapper = new ObjectMapper();
        File schemaFile;
        if(collectionType.equalsIgnoreCase("department")) {
            schemaFile = new File("src/main/resources/Schemas/DepartmentSchema.json");
        }
        else {
            schemaFile = new File("src/main/resources/Schemas/StoreSchema.json");

        }
        String path = schemaFile.getPath();
        String jsonString = new String(Files.readAllBytes(Paths.get(path)));
        out.write(jsonString);
        out.close();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    @SneakyThrows
    public String deleteCollection(String db_name , String collection_name , HttpSession session){
        UserToken user = (UserToken) session.getAttribute("entered");
        URL dist =new URL("http://worker0:8080/api/delete/collection/" + db_name +"/"+collection_name+"/true/true");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("USERNAME" , user.getUsername());
        conn.setRequestProperty("TOKEN" , user.getToken());
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}

