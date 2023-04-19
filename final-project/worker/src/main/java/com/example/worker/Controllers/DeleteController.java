package com.example.worker.Controllers;

import com.example.worker.Affinity.Affinity;
import com.example.worker.Authentication.AuthService;
import com.example.worker.Services.CreationService;
import com.example.worker.Services.QueryService;
import com.example.worker.clusterControl.Broadcast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DeleteController {
    private final Broadcast broadcaster;
    private final CreationService creationService;
    private final QueryService queryService;
    private final Affinity affinity;
    private final AuthService authService;
    @Autowired
    public DeleteController(Broadcast broadcaster, CreationService creationService, QueryService queryService, Affinity affinity, AuthService authService) {
        this.broadcaster = broadcaster;
        this.creationService = creationService;
        this.queryService = queryService;
        this.affinity = affinity;
        this.authService = authService;
    }
    @GetMapping("/api/delete/{db_name}/{Collection}/{id}/{human}/{checkaffinity}")
    @ResponseBody
    public String deleteWithId(@PathVariable("db_name") String database,
                               @PathVariable("Collection") String collection,
                               @PathVariable("id") String id,
                               @PathVariable("checkaffinity") String aff,
                               @RequestHeader("USERNAME") String username,
                               @RequestHeader("TOKEN") String token,
                               @PathVariable("human") String human){

        if(aff.equalsIgnoreCase("true")){
            if(!authService.trusted_Admin(username,token))
                return "user is Unauthorized";
            return affinity.deleteWithId(database,collection,id);
        }
        else if(human.equalsIgnoreCase("true")){
            return broadcaster.deleteRecord(database,collection,id);
        }
        else
            return queryService.deleteById(database,collection,id);
    }
    @DeleteMapping("delete/property/{database}/{collection}/{property}/{value}/{human}/{checkaffinity}")
    @ResponseBody
    public String deleteAllOfValue( @PathVariable("database") String database,
                                    @PathVariable("collection") String collection,
                                    @PathVariable("property") String property,
                                    @PathVariable("value") String value,
                                    @PathVariable("checkaffinity") String aff,
                                    @RequestHeader("USERNAME") String username,
                                    @RequestHeader("TOKEN") String token,
                                    @PathVariable("human") String human){
        if(aff.equalsIgnoreCase("true")){
            if(!authService.trusted_Admin(username,token))
                return "user is Unauthorized";
            return affinity.deleteAllOfValue(database,collection,property,value);
        }
        else if(human.equalsIgnoreCase("true")){
            return broadcaster.deleteAllOfValue(database,collection,property,value);
        }
        else
            return queryService.deleteAllOfValue(database,collection,property,value);
    }

    @DeleteMapping("/api/delete/database/{database}/{human}/{checkaffinity}")
    @ResponseBody
    public String deleteDatabase( @PathVariable("database") String Database,
                                  @PathVariable("human") String human,
                                  @RequestHeader("USERNAME") String username,
                                  @RequestHeader("TOKEN") String token,
                                  @PathVariable("checkaffinity") String aff){
        if(aff.equalsIgnoreCase("true")){
            if(!authService.trusted_Admin(username,token))
                return "user is Unauthorized";
            return affinity.deleteDb(Database);
        }
        else if(human.equalsIgnoreCase("true")) {
            return broadcaster.deleteDatabase(Database);
        }
        else
            return creationService.dropDatabase(Database);
    }

    @DeleteMapping("/api/delete/collection/{database}/{collection}/{human}/{checkaffinity}")
    @ResponseBody
    public String deleteCollection( @PathVariable("database") String Database,
                                    @PathVariable("collection") String Collection,
                                    @PathVariable("human") String human,
                                    @RequestHeader("USERNAME") String username,
                                    @RequestHeader("TOKEN") String token,
                                    @PathVariable("checkaffinity") String aff){
        if(aff.equalsIgnoreCase("true")){
            if(!authService.trusted_Admin(username,token))
                return "user is Unauthorized :401";
            return affinity.deleteColl(Database,Collection);
        }
        else if(human.equalsIgnoreCase("true")) {
            return broadcaster.deleteCollection(Database,Collection);
        }
        else
            return creationService.dropCollection(Database,Collection);
    }
}
