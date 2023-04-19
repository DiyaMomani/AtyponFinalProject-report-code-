package com.example.worker.Affinity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AffinityBroadcast {
    @SneakyThrows
    public String buildDb(String dbName , String port){
        String url = "http://"+port+":8080";
        URL dist = new URL(url + "/api/create/db/" + dbName + "/true/false");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("USERNAME" , "");
        conn.setRequestProperty("TOKEN" , "");
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
    public String buildColl(String dbName, String collectionName , String jsonObject , String port){
        String url = "http://"+port+":8080";
        URL dist = new URL(url + "/api/create/collection/" + dbName + "/" + collectionName +"/true/false");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("USERNAME" , "");
        conn.setRequestProperty("TOKEN" , "");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
        out.write(jsonObject);
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
    public String addRecord(String dbName,String collection , String Json , String port){
        String url = "http://"+port+":8080";
        URL dist = new URL(url + "/api/add/record/" + dbName + "/" + collection +"/true/false");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("USERNAME" , "");
        conn.setRequestProperty("TOKEN" , "");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
        out.write(Json);
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
    public String deleteDb(String dbName, String port){
        String url = "http://"+port+":8080";
        URL dist = new URL(url + "/api/delete/database/" + dbName + "/true/false");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("USERNAME" , "");
        conn.setRequestProperty("TOKEN" , "");
        conn.setRequestProperty("Content-Type", "application/json");
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
    public String deleteColl(String dbName, String collection , String port){
        String url = "http://"+port+":8080";
        URL dist = new URL(url + "/api/delete/collection/" + dbName + "/" + collection +"/true/false");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("USERNAME" , "");
        conn.setRequestProperty("TOKEN" , "");
        conn.setRequestProperty("Content-Type", "application/json");
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
    public String deleteWithId(String dbName,String collection ,String id, String port){
        String url = "http://"+port+":8080";
        URL dist = new URL(url + "/api/delete/" + dbName + "/" + collection + "/" + id +"/true/false");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("USERNAME" , "");
        conn.setRequestProperty("TOKEN" , "");
        conn.setRequestProperty("Content-Type", "application/json");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
        return response.toString();
    }
    @SneakyThrows
    public String deleteAllOfValue(String dbName,String collection , String property,String value,String port){
        String url = "http://"+port+":8080";
        URL dist = new URL(url + "/delete/property/"+dbName+"/"+collection+"/"+property+"/"+value+"/true/false");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("USERNAME" , "");
        conn.setRequestProperty("TOKEN" , "");
        conn.setRequestProperty("Content-Type", "application/json");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();

    }
    @SneakyThrows
    public String update(String database, String collection, String id, JsonNode requestBody, String port){
        String url = "http://"+port+":8080";
        URL dist = new URL(url + "/api/update/"+database+"/"+collection+"/"+id+"/true/false");
        HttpURLConnection conn = (HttpURLConnection) dist.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("USERNAME" , "NotRequired");
        conn.setRequestProperty("TOKEN" , "NotRequired");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        ObjectMapper mapper = new ObjectMapper();
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
        out.write(mapper.writeValueAsString(requestBody));
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
}

