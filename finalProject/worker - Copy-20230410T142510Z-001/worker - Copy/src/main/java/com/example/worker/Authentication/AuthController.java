package com.example.worker.Authentication;

import com.example.worker.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService=new AuthService();
    @GetMapping("/add/user")
    public ApiResponse addUser(@RequestHeader("USERNAME") String username ,
                               @RequestHeader("TOKEN") String token){
        authService.add_User(new UserForm(username,token));
        return new ApiResponse("user Added Succesfully",200);
    }

    @GetMapping("/add/admin")
    public ApiResponse addAdmin(@RequestHeader("USERNAME") String username ,
                           @RequestHeader("TOKEN") String token){
        authService.add_Admin(new UserForm(username,token));
        return new ApiResponse("admin Added Succesfully",200);
    }

    @GetMapping("/check/user")
    public String checkUser(@RequestHeader("USERNAME") String username ,
                            @RequestHeader("TOKEN") String token){
        if(authService.trusted_User(username,token))
            return "yes";
        else
            return "no";
    }
    @GetMapping("/check/admin")
    public String checkAdmin(@RequestHeader("USERNAME") String username ,
                             @RequestHeader("TOKEN") String token){
        if(authService.trusted_Admin(username,token))
            return "yes";
        else
            return "no";
    }

}
