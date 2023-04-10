package com.example.worker.Controllers;

import com.example.worker.Authentication.AuthService;
import com.example.worker.Services.QueryService;
import com.example.worker.clusterControl.CheckerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class queryController {

    private final QueryService queryService;
    private final AuthService authService;
    private final CheckerService checkerService;
    @Autowired
    public queryController(QueryService queryService ,AuthService authService, CheckerService checkerService){
        this.queryService = queryService;
        this.authService = authService;
        this.checkerService = checkerService;
    }

    @SneakyThrows
    @GetMapping("api/get/AllRecords/{db_name}/{Collection}")
    public ArrayNode getAll(@PathVariable("db_name") String database,
                            @PathVariable("Collection") String Collection,
                            @RequestHeader("USERNAME") String username,
                            @RequestHeader("TOKEN") String token){

        if(!authService.trusted_Admin(username,token) && !authService.trusted_User(username,token)){
            ObjectMapper objectMapper=new ObjectMapper();
            return objectMapper.createArrayNode();
        }
        ArrayNode node = queryService.getAll(database,Collection).get();
        return node;
    }


    @GetMapping("/api/{db_name}/{Collection}/{property}/{value}")
    @ResponseBody
    public List<ObjectNode> getByProperty(@PathVariable("db_name") String database,
                                          @PathVariable("Collection") String Collection,
                                          @PathVariable("property") String Property,
                                          @RequestHeader("USERNAME") String username,
                                          @RequestHeader("TOKEN") String token,
                                          @PathVariable("value") String value){
        if(!authService.trusted_Admin(username,token) && !authService.trusted_User(username,token)) {
            List<ObjectNode>list =new ArrayList<>();
            return list;
        }

        List<ObjectNode>list= null;
        try {
            list = queryService.getByProperty(database,Collection,Property,value).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    @GetMapping("/api/getCollection/{dbName}")
    @ResponseBody
    public List<String> getCollections(@PathVariable("dbName") String database,
                                       @RequestHeader("USERNAME") String username,
                                       @RequestHeader("TOKEN") String token
    ){
        if(!authService.trusted_Admin(username,token) && !authService.trusted_User(username,token)){
            List<String>list =new ArrayList<>();
            return list;
        }
        List<String>list= null;
        try {
            list=queryService.getCollections(database).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    @GetMapping("/api/getDatabase")
    @ResponseBody
    public List<String> getDatabase(@RequestHeader("USERNAME") String username,
                                    @RequestHeader("TOKEN") String token){
        if(!authService.trusted_Admin(username,token) && !authService.trusted_User(username,token)){
            List<String>list =new ArrayList<>();
            return list;
        }
        List<String> list=null;
        try {
            list=queryService.getDatabases().get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @GetMapping("/api/get/{db_name}/{Collection}/{id}")
    @ResponseBody
    public ObjectNode getById(@PathVariable("db_name") String database,
                              @PathVariable("Collection") String Collection,
                              @RequestHeader("USERNAME") String username,
                              @RequestHeader("TOKEN") String token,
                              @PathVariable("id") String id) {
        if(!authService.trusted_Admin(username,token) && !authService.trusted_User(username,token))
            return new ObjectNode(null);
        else {
            ObjectNode object = null;
            try {
                object = queryService.getById(database, Collection, id).get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            return object;
        }
    }
    @GetMapping("api/check/{db_name}")
    @ResponseBody
    public boolean checkDatabase(@PathVariable("db_name") String db_name){

        return checkerService.checkDB(db_name);
    }
    @GetMapping("api/check/{db_name}/{collection}")
    @ResponseBody
    public boolean checkCollection(@PathVariable("db_name")String db_name,
                                   @PathVariable("collection")String collection){
        return checkerService.checkCollection(db_name,collection);
    }
    @GetMapping("api/check/{db_name}/{collection}/{Id}")
    @ResponseBody
    public boolean checkId(@PathVariable("db_name")String db_name,
                           @PathVariable("collection")String collection,
                           @PathVariable("Id") String Id){
        return checkerService.checkById(db_name,collection,Id);
    }



}
