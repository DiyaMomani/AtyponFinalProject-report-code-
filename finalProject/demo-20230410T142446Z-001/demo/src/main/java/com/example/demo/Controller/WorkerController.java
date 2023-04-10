package com.example.demo.Controller;

import com.example.demo.Model.UserWorker;
import com.example.demo.Service.JsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkerController {
    /*@Autowired
    private JsonService jsonService;
    @GetMapping("/add/user")
    public String addUser(@RequestHeader("USERNAME") String username,
                        @RequestHeader("WORKER") String worker,
                          Model model){
        jsonService.addUser(new UserWorker(username,worker));
        model.addAttribute("result" , "adding successfully");
        return "response";
    }*/
}
