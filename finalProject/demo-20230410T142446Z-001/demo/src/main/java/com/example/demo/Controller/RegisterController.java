package com.example.demo.Controller;

import com.example.demo.Service.AuthenticationService;
import com.example.demo.Service.JsonService;
import com.example.demo.Model.UserToken;
import com.example.demo.Model.UserWorker;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {
    private String type;
    @Autowired
    private HttpSession session;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    JsonService jsonService;

    @GetMapping("/")
    public String home(){
        return "homePage";
    }

    @GetMapping("/loginPageAdmin")
    public String loginAdmin(){
        if(type=="user") return "homePage";
        else {
            UserToken userToken = (UserToken) session.getAttribute("entered");
            if (userToken != null)
                return "chooseOperationAdmin";
            return "loginPageAdmin";
        }
    }
    @GetMapping("/loginPageUser")
    public String loginUser(){
        if(type=="admin") return "homePage";
        else {
            UserToken userToken = (UserToken) session.getAttribute("entered");
            if (userToken != null)
                return "chooseOperationUser";
            return "loginPageUser";
        }
    }
    @GetMapping("/signUpAdmin")
    public String getSignUpAdmin(){
        UserToken userToken =(UserToken)session.getAttribute("entered");
        if(userToken !=null)
            return "chooseOperationAdmin";
        return "signUpAdmin";
    }
    @GetMapping("/signUpUser")
    public String getSignUpUser(){
        UserToken userToken =(UserToken)session.getAttribute("entered");
        if(userToken !=null)
            return "chooseOperationUser";
        return "signUpUser";
    }
    @PostMapping("/signUpAdmin")
    public String postSignUpAdmin(@RequestParam("username") String username,
                                  Model model ){
        UserToken userToken =(UserToken)session.getAttribute("entered");
        if(userToken !=null)
            return "chooseOperationAdmin";
        String token=authenticationService.requestToken(username,"admin");
        model.addAttribute("result","("+token +")"+"If you take a token between the brackets,then go back and sign in. ");
        return "responsePage";
    }
    @PostMapping("/signUpUser")
    public String postSignUpUser(@RequestParam("username") String username,
                                  Model model ){
        UserToken userToken =(UserToken)session.getAttribute("entered");
        if(userToken !=null)
            return "chooseOperationUser";
        String token=authenticationService.requestToken(username,"user");
        model.addAttribute("result","("+token +")"+"If you take a token between the brackets,then go back and sign in. ");
        return "responsePage";
    }
    @GetMapping("/signInAdmin")
    public String getSignInAdmin(){
        UserToken userToken =(UserToken)session.getAttribute("entered");
        if(userToken !=null)
            return "chooseOperationAdmin";
        return "signInPageAdmin";
    }
    @GetMapping("/signInUser")
    public String getSignInUser(){
        UserToken userToken =(UserToken)session.getAttribute("entered");
        if(userToken !=null)
            return "chooseOperationUser";
        return "signInPageUser";
    }
    @PostMapping("/signInAdmin")
    public String postSignInAdmin(@RequestParam("username") String username,
                             @RequestParam("token") String token,
                             Model model){
        if(authenticationService.checkAdmin(username,token)){
            type="admin";
            UserToken userToken = new UserToken(username,token);
            String worker  =jsonService.getWorker(username);
            session.setAttribute("entered",userToken);
            session.setAttribute("worker",worker);
            return "chooseOperationAdmin";
        }
        model.addAttribute("result","Invalid Admin");
        return "responsePage";
    }
    @PostMapping("/signInUser")
    public String postSignInUser(@RequestParam("username") String username,
                                  @RequestParam("token") String token,
                                  Model model){
        if(!authenticationService.checkUsername(username)){
            model.addAttribute("result" , "you are not signing in");
            return "responsePage";
        }
        if(!authenticationService.checkTokenUser(username,token)){
            model.addAttribute("result" , "Invalid token");
            return "responsePage";
        }

        type="user";
        UserToken userToken = new UserToken(username,token);
        String worker  =jsonService.getWorker(username);
        session.setAttribute("entered",userToken);
        session.setAttribute("worker",worker);
        return "chooseOperationUser";
    }
    @GetMapping("/add/user")
    public String addUser(@RequestHeader("USERNAME") String username,
                          @RequestHeader("WORKER") String worker,
                          Model model){
        jsonService.addUser(new UserWorker(username,worker));
        model.addAttribute("result" , "adding successfully");
        return "responsePage";
    }


}
