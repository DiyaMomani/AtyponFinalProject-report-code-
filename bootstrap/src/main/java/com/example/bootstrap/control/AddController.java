package com.example.bootstrap.control;

import com.example.bootstrap.Affinity.AffinityService;
import com.example.bootstrap.Authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/boot/")
public class AddController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private AffinityService affinityService;
    @GetMapping("/add/user")
    public String addUser(@RequestHeader("USERNAME") String username){
        if(authenticationService.isExistUser(username))
            return "already exist";
        String token = UUID.randomUUID().toString();
        System.out.println(token);
        affinityService.addUser(username,token);
        return token;
    }

    @GetMapping("/add/admin")
    public String addAdmin(@RequestHeader("USERNAME") String username){
        if(authenticationService.isExistAdmin(username))
            return "already exist";
        String token = UUID.randomUUID().toString();
        affinityService.addAdmin(username,token);
        return token;
    }

}
