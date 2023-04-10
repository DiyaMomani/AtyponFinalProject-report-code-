package com.example.demo.Controller;


import com.example.demo.Model.UserToken;
import com.example.demo.Service.DatabaseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/database")
public class DatabaseController {
    private String type;
    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private HttpSession session;
    @GetMapping("/create")
    public String getCreateDB(){
        type="user";
        UserToken userToken = (UserToken) session.getAttribute("entered");
        return "create-db";
    }
    @PostMapping("/create")
    public String postCreateDB(@RequestParam("db_name") String db_name ,
                               HttpSession session,
                               Model model){
        UserToken userToken = (UserToken) session.getAttribute("entered");
        if(!databaseService.isExist(db_name,session)) {
            databaseService.createDatabase(db_name,session);
            model.addAttribute("result", "Building With Name "+db_name+" Created Successfully");
        }
        else
            model.addAttribute("result","Building With Name "+db_name+" Already Exist");
        return "responsePage";
    }

    @GetMapping("/delete")
    public String getDeleteDB(){

        type="admin";
        UserToken userToken = (UserToken) session.getAttribute("entered");

        return "delete-db";
    }
    @PostMapping("/delete")
    public String postDeleteDB(@RequestParam("db_name") String db_name,
                               HttpSession session,
                               Model model){
        type="admin";
        UserToken userToken = (UserToken) session.getAttribute("entered");

        String response=databaseService.deleteDatabase(db_name,session);
        model.addAttribute("result" , response);
        return "responsePage";
    }

    @GetMapping("/list")
    public String getList(HttpSession session, Model model){
        UserToken userToken = (UserToken) session.getAttribute("entered");

        String list = databaseService.getList(session);
        model.addAttribute("result",list);
        return "responsePage";
    }
}
