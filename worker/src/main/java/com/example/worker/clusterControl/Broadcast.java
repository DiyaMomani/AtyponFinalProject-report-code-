package com.example.worker.clusterControl;

import com.example.worker.ApiResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class Broadcast {
    private String []urls = {"http://worker0:8080" , "http://worker1:8080" , "http://worker2:8080" , "http://worker3:8080"};

    public String BuildDataBase(String database){
        for(String url: urls) {
            try {
                URL dist = new URL(url + "/api/create/db/" + database + "/false/false");
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

                System.out.println(response.toString());

            } catch (Exception e) {
                return "broadCast failed : 500";
            }
        }
        return "broadCast done : 200";
    }
    public String buildCollection(String database,String collection, String Json){
        for(String Url : urls) {
            try {
                URL url = new URL(Url + "/api/create/collection/" + database + "/" + collection+ "/false/false");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("USERNAME" , "");
                conn.setRequestProperty("TOKEN" , "");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                byte[] input = Json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println(response.toString());

            } catch (Exception e) {
                return "broadCast failed : 500";
            }
        }
        return "broadCast done : 200";
    }
    public String addRecord(String database, String collection, String Json){
        for(String Url : urls){
            try {
                URL url = new URL(Url + "/api/add/record/" + database +"/"+ collection + "/false/false");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("USERNAME" , "");
                conn.setRequestProperty("TOKEN" , "");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                byte[] input = Json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println(response.toString());
                if(!response.toString().equalsIgnoreCase("record added successfully")){
                    return response.toString();
                }
            } catch (Exception e) {
                return "broadCast failed";
            }

        }
        return "Adding Done Successfully";
    }
    public ApiResponse deleteRecord(String database,String collection, String id){
        for(String Url : urls){
            try {
                URL url = new URL( Url+ "/api/delete/" + database + "/" + collection+ "/" + id + "/false/false");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

                System.out.println(response.toString());

            } catch (Exception e) {
                return new ApiResponse("broadCast failed",500);
            }
        }
        return new ApiResponse("broadCast done",200);
    }
    public String Update(String database, String Collection, String id, JsonNode requestBody){
        for(String Url : urls){
            try {
                URL dist = new URL(Url + "/api/update/"+database+"/"+ Collection + "/" + id +"/false/false");
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

                System.out.println(response.toString());
                if(!response.toString().equalsIgnoreCase("Update done"))
                    return response.toString();

            } catch (Exception e) {
                return "broadCast failed";
            }
        }
        return "Updating all nodes done";
    }

    public ApiResponse deleteDatabase(String db_name){
        for(String Url : urls){
            try {
                URL url = new URL(Url + "/api/delete/database/"+db_name+"/false/false");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
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

                System.out.println(response.toString());

            } catch (Exception e) {
                return new ApiResponse("broadCast failed",500);
            }
        }
        return new ApiResponse("broadCast done",200);
    }

    public String deleteCollection(String db_name , String collection){
        for(String Url : urls){
            try {
                URL url = new URL(Url + "/api/delete/collection/"+db_name+"/" + collection +"/false/false");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
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
                System.out.println(response.toString());
            } catch (Exception e) {
                return "broadCast failed : 500";
            }
        }
        return "broadCast done : 200";
    }
    public ApiResponse deleteAllOfValue(String dbName,String collection , String property,String value){
        for(String Url : urls){
            try {
                URL url = new URL(Url + "/delete/property/"+dbName+"/"+collection+"/"+property+"/"+value +"/false/false");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
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
                System.out.println(response.toString());
            } catch (Exception e) {
                return new ApiResponse("broadCast failed",500);
            }
        }
        return new ApiResponse("broadCast done",200);
    }
    public boolean checkUpdate(String database,String collection,String id,String property,String oldValue){
        boolean valid=true;
        for(String Url : urls){
            try {
                URL url = new URL(Url + "/checkUpdate/"+database+ "/" +collection+ "/" +id+ "/" +property+ "/" +oldValue);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                valid = valid & Boolean.parseBoolean(response.toString());
                in.close();
            }catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                return false;
            }
        }
        return valid;
    }


}
