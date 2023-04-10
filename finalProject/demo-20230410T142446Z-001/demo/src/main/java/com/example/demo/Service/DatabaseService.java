package com.example.demo.Service;

import com.example.demo.Model.UserToken;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class DatabaseService {
    private JsonService jsonService=new JsonService();
    @SneakyThrows
    public String createDatabase(String db_name , HttpSession session){
        UserToken user = (UserToken) session.getAttribute("entered");
        String workerId = jsonService.getWorker(user.getUsername());
        URL dist =new URL("http://localhost:808"+workerId+"/api/create/db/" + db_name +"/true/true");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("GET");
        System.out.println(user.getUsername());
        System.out.println(user.getToken());
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
    public String deleteDatabase(String db_name , HttpSession session){
        UserToken user = (UserToken) session.getAttribute("entered");
        System.out.println(user);
        String workerId = jsonService.getWorker(user.getUsername());
        URL dist =new URL("http://localhost:8080" +"/api/delete/database/" + db_name +"/true/true");
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

    @SneakyThrows
    public String getList(HttpSession session){
        UserToken user = (UserToken) session.getAttribute("entered");
        String workerId = jsonService.getWorker(user.getUsername());
        URL dist =new URL("http://localhost:8080"+"/api/getDatabase");
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
    public boolean isExist(String db_name, HttpSession session){
        UserToken user = (UserToken)session.getAttribute("entered");
        String workerId = jsonService.getWorker(user.getUsername());
        URL dist =new URL("http://localhost:808"+workerId+"/api/check/" + db_name);
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
        return response.toString().equals("true");
    }
}
