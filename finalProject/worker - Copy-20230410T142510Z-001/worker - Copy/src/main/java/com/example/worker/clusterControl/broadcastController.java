package com.example.worker.clusterControl;

import com.example.worker.ApiResponse;
import com.example.worker.Services.CreationService;
import com.example.worker.Services.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class  broadcastController {
    private final CheckerService checkerService;

    @Autowired
    public broadcastController(CheckerService checkerService) {
        this.checkerService = checkerService;
    }
    @GetMapping("cluster/check/db/{db_name}")
    public boolean CheckDB(@PathVariable("db_name") String database){
        return checkerService.checkDB(database);
    }
    @GetMapping("cluster/check/record/{db_name}/{Collection}")
    public boolean checkId(@PathVariable("db_name") String database,@PathVariable("Collection") String Collection, @RequestBody String Json){
        return checkerService.checkId(database,Collection,Json);
    }


}
