package com.example.demo.Controller;

import com.example.demo.Model.UserToken;
import com.example.demo.Service.CollectionService;
import com.example.demo.Service.DatabaseService;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/collection")
public class CollectionController {
    @Autowired
    private CollectionService collectionService;

    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private HttpSession session;
    @GetMapping("/create")
    public String getCreateCollection(){

        UserToken userToken = (UserToken) session.getAttribute("entered");
        return "create-collection";
    }
    @SneakyThrows
    @PostMapping("/create")
    public String postCreateCollection(@RequestParam("building-name") String building_name,
                                       @RequestParam("permission") String collectionType, HttpSession session,
                                       Model model) {
        String response=collectionService.createCollection(building_name,collectionType,session);

            model.addAttribute("result",response);
        return "responsePage";
    }
    @GetMapping("/delete")
    public String getDeleteCollection(){
        UserToken userToken = (UserToken) session.getAttribute("entered");
        return "delete-collection";
    }
    @PostMapping("/delete")
    public String postDeleteCollection(@RequestParam("db_name") String db_name,
                                        @RequestParam("collection_name") String collection_name,
                                       HttpSession session , Model model){

        UserToken userToken = (UserToken) session.getAttribute("entered");

        String response = collectionService.deleteCollection(db_name,collection_name,session);
        model.addAttribute("result",response);
        return "responsePage";
    }
    //TODO:move
    @GetMapping("/facilityType")
    public String facilityTypeGet(){
        UserToken userToken = (UserToken) session.getAttribute("entered");
        return "facilityType";
    }
}
