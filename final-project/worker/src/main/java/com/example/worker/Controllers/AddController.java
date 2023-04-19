package com.example.worker.Controllers;

import com.example.worker.Affinity.Affinity;
import com.example.worker.Authentication.AuthService;
import com.example.worker.Services.CreationService;
import com.example.worker.Services.QueryService;
import com.example.worker.clusterControl.Broadcast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AddController {
    private final Broadcast broadcaster;
    private final CreationService creationService;
    private final QueryService queryService;
    private final Affinity affinity;
    private final AuthService authService;
    @Autowired
    public AddController(Broadcast broadcaster, CreationService creationService, QueryService queryService, Affinity affinity, AuthService authService) {
        this.broadcaster = broadcaster;
        this.creationService = creationService;
        this.queryService = queryService;
        this.affinity = affinity;
        this.authService = authService;
    }
    @GetMapping("/api/create/db/{db_name}/{human}/{checkaffinity}")
    @ResponseBody
    public String createDatabase(@PathVariable("db_name") String dbName
            ,@PathVariable("human") String human
            ,@PathVariable("checkaffinity") String aff,
                                 @RequestHeader("USERNAME") String username,
                                 @RequestHeader("TOKEN") String token) {
        if(aff.equalsIgnoreCase("true")){
            if(!authService.trusted_User(username,token) && !authService.trusted_Admin(username,token))
                return "user is Unauthorized";
            return affinity.BuildDb(dbName);
        }
        else if(human.equalsIgnoreCase("true")) {

            return broadcaster.BuildDataBase(dbName);

        }
        else
            return creationService.initDatabase(dbName);
    }

    @PostMapping("/api/create/collection/{db_name}/{Collection}/{human}/{checkaffinity}")
    @ResponseBody
    public String createCollection(@PathVariable("db_name") String database,
                                   @PathVariable("Collection") String collection,
                                   @PathVariable("human") String human,
                                   @PathVariable("checkaffinity") String aff,
                                   @RequestHeader("USERNAME") String username,
                                   @RequestHeader("TOKEN") String token,
                                   @RequestBody String Json) {

        if(aff.equalsIgnoreCase("true")){
            if(!authService.trusted_User(username,token) && !authService.trusted_Admin(username,token))
                return "user is Unauthorized";
            return affinity.collAffinity(database,collection,Json);
        }
        else if(human.equalsIgnoreCase("true")) {
            return broadcaster.buildCollection(database,collection,Json);

        }
        else
            return creationService.initCollection(database,collection,Json);
    }

    @PostMapping("/api/add/record/{db_name}/{Collection}/{human}/{checkaffinity}")
    @ResponseBody
    public String addRecord(@PathVariable("db_name") String database,
                            @PathVariable("Collection") String Collection,
                            @PathVariable("human") String human,
                            @PathVariable("checkaffinity") String aff,
                            @RequestHeader("USERNAME") String username,
                            @RequestHeader("TOKEN") String token,
                            @RequestBody String Json){

        if(aff.equalsIgnoreCase("true")){
            if(!authService.trusted_User(username,token) && !authService.trusted_Admin(username,token))
                return "user is Unauthorized";
            return affinity.addRecord(database,Collection,Json);
        }
        else if(human.equalsIgnoreCase("true")) {
            return broadcaster.addRecord(database,Collection,Json);
        }
        else
            return creationService.addRecord(database,Collection,Json);

    }
}
