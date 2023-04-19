package com.example.demo.Service;

import com.example.demo.Building.Department;
import com.example.demo.Building.Store;
import com.example.demo.Model.UserToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class RecordService {
    private JsonService jsonService=new JsonService();
    @SneakyThrows
    public String getList(String db_name ,
                          String collection_name,
                          HttpSession session ){

        UserToken user = (UserToken) session.getAttribute("entered");
        String workerId = jsonService.getWorker(user.getUsername());
        if(workerId.charAt(0) < '0' || workerId.charAt(0) > '4')
            workerId = "0";
        URL dist =new URL("http://worker"+workerId+":8080/api/get/AllRecords/" + db_name +"/" + collection_name);
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
    private String getSchema(String db_name , String collection_name ,HttpSession session) {
        UserToken user = (UserToken) session.getAttribute("login");
        String workerId = jsonService.getWorker(user.getUsername());
        if(workerId.charAt(0) < '0' || workerId.charAt(0) > '4')
            workerId = "0";
        URL dist =new URL("http://worker"+workerId+":8080/worker/document/schema/" + db_name +"/" + collection_name);
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
    public String deleteRecord(String db_name , String collection_name , String id , HttpSession session){
        UserToken user = (UserToken) session.getAttribute("entered");
        String workerId = jsonService.getWorker(user.getUsername());
        if(workerId.charAt(0) < '0' || workerId.charAt(0) > '4')
            workerId = "0";
        URL dist =new URL("http://worker"+workerId+":8080/api/delete/" + db_name +"/" + collection_name + "/" + id + "/true/true");
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
    public String updateRecord(String db_name , String collection_name , String id ,
                               String prop, String value , HttpSession session){
        UserToken user = (UserToken) session.getAttribute("entered");
        String workerId = jsonService.getWorker(user.getUsername());
        if(workerId.charAt(0) < '0' || workerId.charAt(0) > '4')
            workerId = "0";
        URL dist =new URL("http://worker" + workerId + ":8080/api/update/" +
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
    public String addDepartment(String building, String id,String roomNum, String bathroomNum, HttpSession session){
        UserToken user = (UserToken) session.getAttribute("entered");
        String workerId= jsonService.getWorker(user.getUsername());
        if(workerId.charAt(0) < '0' || workerId.charAt(0) > '4')
            workerId = "0";
        URL dist =new URL("http://worker"+workerId+":8080/api/add/record/"+building+"/department/true/true");
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
        return response.toString();
    }

    @SneakyThrows
    public String addStore(String building, String id, String area, String height, HttpSession session){
        UserToken user = (UserToken) session.getAttribute("entered");
        String workerId= jsonService.getWorker(user.getUsername());
        if(workerId.charAt(0) < '0' || workerId.charAt(0) > '4')
            workerId = "0";
        URL dist =new URL("http://worker"+workerId+":8080/api/add/record/"+building+"/store/true/true");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("USERNAME" ,user.getUsername());
        conn.setRequestProperty("TOKEN" , user.getToken());
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        Store store=new Store(id, area, height);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(store);
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
        return response.toString();
    }

}
