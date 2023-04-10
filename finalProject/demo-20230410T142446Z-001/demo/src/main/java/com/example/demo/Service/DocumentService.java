package com.example.demo.Service;

import com.example.demo.Building.Department;
import com.example.demo.Building.Store;
import com.example.demo.Model.UserToken;
import com.example.demo.Model.UserWorker;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocumentService {
    private JsonService jsonService=new JsonService();
    @SneakyThrows
    public String getList(String db_name ,
                          String collection_name,
                          HttpSession session ){

        UserToken user = (UserToken) session.getAttribute("entered");
        String workerId = jsonService.getWorker(user.getUsername());
        URL dist =new URL("http://localhost:808"+workerId+"/api/get/AllRecords/" + db_name +"/" + collection_name);
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("GET");
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
    @SneakyThrows
    public String addDocument(String db_name , String collection_name , Map<String,String> prop,
                              HttpSession session){
        //TODO :: add code
        UserToken user = (UserToken) session.getAttribute("login");
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> jsonMap = new HashMap<>();
        for (Map.Entry<String, String> entry : prop.entrySet()) {
            if(entry.getKey().equals("db_name") || entry.getKey().equals("collection_name")) {
                continue;
            }
            jsonMap.put(entry.getKey() , entry.getValue());
        }
        String json = mapper.writeValueAsString(jsonMap);
        System.out.println("json -> " + json);
        String workerId = jsonService.getWorker(user.getUsername());
        URL dist =new URL("http://localhost:808"+workerId+"/api/add/record/" + db_name +"/" + collection_name + "/true/true");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("USERNAME" , user.getUsername());
        conn.setRequestProperty("TOKEN" , user.getToken());
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
        out.write(json);
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
    public List<String> schemaProp(String db_name , String collection_name, HttpSession session){
        String schema = getSchema(db_name,collection_name,session);
        System.out.println("->" + schema);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode schemaNode = objectMapper.readTree(schema);
        Map<String, Object> propertiesMap = new HashMap<>();
        JsonNode propertiesNode = schemaNode.get("properties");
        if (propertiesNode != null) {
            propertiesNode.fields().forEachRemaining(entry -> {
                String propertyName = entry.getKey();
                JsonNode propertyNode = entry.getValue();
                Map<String, Object> propertyMap = new HashMap<>();
                propertyMap.put("type", propertyNode.get("type").asText());
                // Add any additional property fields here
                propertiesMap.put(propertyName, propertyMap);
            });
        }
        List<String> prop = new ArrayList<>();
        for(Map.Entry<String,Object> entry : propertiesMap.entrySet()){
            prop.add(entry.getKey());
        }
        return prop;
    }

    @SneakyThrows
    private String getSchema(String db_name , String collection_name ,HttpSession session) {
        UserToken user = (UserToken) session.getAttribute("login");
        String workerId = jsonService.getWorker(user.getUsername());
        URL dist =new URL("http://localhost:808"+workerId+"/worker/document/schema/" + db_name +"/" + collection_name);
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("GET");
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
    public String deleteDocument(String db_name , String collection_name , String id , HttpSession session){
        UserToken user = (UserToken) session.getAttribute("login");
        String workerId = jsonService.getWorker(user.getUsername());
        URL dist =new URL("http://localhost:808"+workerId+"/api/delete/" + db_name +"/" + collection_name + "/" + id + "/true/true");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("GET");
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

    @SneakyThrows
    public String updateDocument(String db_name , String collection_name , String id ,
                                 String prop, String value ,  HttpSession session){
        UserToken user = (UserToken) session.getAttribute("login");
        String workerId = jsonService.getWorker(user.getUsername());
        URL dist =new URL("http://localhost:808" + workerId + "/api/update/" +
                db_name +"/" + collection_name + "/" + id + "/true/true");

        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("USERNAME" ,user.getUsername());
        conn.setRequestProperty("TOKEN" , user.getToken());
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode payload = mapper.createObjectNode();
        payload.put("Property", prop);
        payload.put("value", value);
        // Write the payload to the request body
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
        out.write(mapper.writeValueAsString(payload));
        out.flush();


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
    public boolean addDepartment(String building, String id,String roomNum, String bathroomNum, HttpSession session){
        UserToken user = (UserToken) session.getAttribute("entered");
        String workerId= session.getAttribute("worker").toString();
        URL dist =new URL("http://localhost:808"+workerId+"/api/add/record/"+building+"/department/true/true");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("USERNAME" ,user.getUsername());
        conn.setRequestProperty("TOKEN" , user.getToken());
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        Department department=new Department(id,roomNum,bathroomNum);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(department);
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
        out.write(json);
        System.out.println(json);
        out.close();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString().equalsIgnoreCase("Adding Done Successfully");


    }
    @SneakyThrows
    public boolean isExist(String db_name, String collection, String Id,HttpSession session){
        UserToken user = (UserToken)session.getAttribute("entered");
        String workerId = jsonService.getWorker(user.getUsername());
        URL dist =new URL("http://localhost:808"+workerId+"/api/check/" + db_name+ "/" +collection + "/" + Id);
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString().equals("true");
    }
}
