package com.example.worker.Controllers;

import com.example.worker.Affinity.Affinity;
import com.example.worker.ApiResponse;
import com.example.worker.Authentication.AuthService;
import com.example.worker.Services.QueryService;
import com.example.worker.clusterControl.Broadcast;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;


@RestController
public class UpdateController {
    @Autowired
    private AuthService authService;
    @Autowired
    private QueryService queryService;
    @Autowired
    private Broadcast broadcaster;

    @PutMapping("/api/update/{db_name}/{Collection}/{id}/{human}/{checkaffinity}")
    @ResponseBody
    public String Update(@PathVariable("db_name") String Database,
                         @PathVariable("Collection") String Collection,
                         @PathVariable("id") String id,
                         @PathVariable ("human") String human,
                         @PathVariable ("checkaffinity") String aff,
                         @RequestHeader("USERNAME") String username,
                         @RequestHeader("TOKEN") String token,
                         @RequestBody JsonNode requestBody) {

        // Extract the Property and value parameters from the request body
        String Property = requestBody.get("Property").asText();
        String value = requestBody.get("value").asText();

        if(aff.equalsIgnoreCase("true")){
            if(!authService.trusted_Admin(username,token))
                return "Admin is Unauthorized";

            return Affinity.getInstance().update(Database,Collection,id,requestBody);
        }
        else if(human.equalsIgnoreCase("true")) {
            String oldValue = "#";
            try {
                ObjectNode objectNode = queryService.getById(Database,Collection,id).get();
                oldValue = objectNode.get(Property).asText();
            } catch (Exception e) {
                return "query failed";
            }

            if (broadcaster.checkUpdate(Database, Collection, id, Property, oldValue))
                return broadcaster.Update(Database, Collection, id, requestBody);
            else
                return "check is invalid";
        }
        else
            return queryService.update(Database, Collection, id, Property, value);
    }

    @GetMapping("/checkUpdate/{db_name}/{collection}/{id}/{property}/{value}")
    public boolean Update(@PathVariable("db_name")String database,
                          @PathVariable("collection") String collection,
                          @PathVariable("id") String id,
                          @PathVariable("property") String property,
                          @PathVariable("value") String oldValue){

        return queryService.checkUpdate(database,collection,id,property,oldValue);
    }
}
