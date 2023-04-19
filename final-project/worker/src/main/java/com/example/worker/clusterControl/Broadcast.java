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
                if(!response.toString().equalsIgnoreCase("Database created successfully")){
                    return response.toString();
                }

            } catch (Exception e) {
                return "creating database failed";
            }
        }
        return "Database: "+database+" created successfully";
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
                if(!response.toString().equalsIgnoreCase("Collection created successfully")) {
                    return response.toString();
                }

            } catch (Exception e) {
                return "creating collection failed";
            }
        }
        return "collection: "+collection+" created successfully";
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

                if(!response.toString().equalsIgnoreCase("record added successfully")){
                    return response.toString();
                }
            } catch (Exception e) {
                return "Adding record failed";
            }

        }
        return "Adding record to "+database+" and "+collection+" Done successfully";
    }
    public String deleteRecord(String database,String collection, String id){
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


                if(!response.toString().equalsIgnoreCase("delete done"))
                    return response.toString();

            } catch (Exception e) {
                return "deleting record failed";
            }
        }
        return "deleting record from "+ database+" and "+collection+" Done successfully";
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

                if(!response.toString().equalsIgnoreCase("Update done"))
                    return response.toString();

            } catch (Exception e) {
                return "update failed";
            }
        }
        return "Updating record with id: "+id+" in database: "+database+" and "+" collection: "+Collection+ " Done successfully";
    }

    public String deleteDatabase(String db_name){
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


                if(!response.toString().equalsIgnoreCase("dropping Database done"))
                    return response.toString();

            } catch (Exception e) {
                return "deleting failed";
            }
        }
        return "Delete database "+db_name+" done";
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

                if(response.toString().equalsIgnoreCase("dropping Collection done"))
                    return  response.toString();

            } catch (Exception e) {
                return "Deleting collection "+collection+" failed";
            }
        }
        return "deleting collection "+collection+" done ";
    }
    public String deleteAllOfValue(String dbName,String collection , String property,String value){
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
                if( response.toString().equalsIgnoreCase("deleting all " +property +':' + value +" done"))
                    return response.toString();

            } catch (Exception e) {
                return "deleting all with " + property + ":" + value + " failed";
            }
        }
        return "deleting all with " + property + ":" + value + "from all Nodes done";
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
