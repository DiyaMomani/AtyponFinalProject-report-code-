package com.example.demo.Controller;


import com.example.demo.Model.UserToken;
import com.example.demo.Service.CollectionService;
import com.example.demo.Service.DatabaseService;
import com.example.demo.Service.DocumentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/document")
public class DocumentController {
    private String type;
    @Autowired
    private CollectionService collectionService;

    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private HttpSession session;

    @PostMapping("/listOfDepartment")
    public String ListOfDepartment(@RequestParam("db_name") String db_name,
                           HttpSession session , Model model){
        UserToken userToken = (UserToken) session.getAttribute("entered");
        String list = documentService.getList(db_name,"department",session);

        model.addAttribute("result",list);
        return "responsePage";
    }
    @PostMapping("/listOfStore")
    public String ListOfStore(@RequestParam("db_name") String db_name,
                              HttpSession session , Model model){
        UserToken userToken = (UserToken) session.getAttribute("entered");
        String list = documentService.getList(db_name,"store",session);

        model.addAttribute("result",list);
        return "responsePage";
    }
    @GetMapping("/add")
    public String addDocument(){
        type="user";
        UserToken userToken = (UserToken) session.getAttribute("entered");
        return "document";
    }

    @PostMapping("/add")
    public String postAddDocument(@RequestParam("db_name") String db_name,
                                  @RequestParam("collection_name") String collection_name,
                                  HttpSession session,Model model){
        type="user";
        UserToken userToken = (UserToken) session.getAttribute("entered");
        if(!collectionService.isExist(db_name,collection_name,session) ||
                !databaseService.isExist(db_name,session)){
            model.addAttribute("result","no database or collection");
            return "response";
        }
        String path = "/document/add/" + db_name + "/" + collection_name;
        model.addAttribute("path",path);
        model.addAttribute("propertyList",documentService.schemaProp(db_name,collection_name,session));
        return "add-document";
    }

    @GetMapping("/delete")
    public String getDelete(){
        type="admin";
        UserToken userToken = (UserToken) session.getAttribute("entered");
        if (userToken == null)
            return "chooseOperationAdmin";
        return "document-delete";
    }

    @PostMapping("/delete")
    public String postDelete(@RequestParam ("building_name") String db_name,
                             @RequestParam ("permission") String collection_name,
                             @RequestParam("id") String id,
                             HttpSession session , Model model){
        type="admin";
        UserToken userToken = (UserToken) session.getAttribute("entered");
        if (userToken == null)
            return "chooseOperationAdmin";
        if(!collectionService.isExist(db_name,collection_name,session) ||
                !databaseService.isExist(db_name,session)){
            model.addAttribute("result","no database or collection");
            return "responsePage";
        }
        String response = documentService.deleteDocument(db_name,collection_name, id ,session);
        model.addAttribute("result",response);
        return "responsePage";
    }

    @GetMapping("/update")
    public String getUpdate(){
        type="admin";
        UserToken userToken = (UserToken) session.getAttribute("entered");
        if (userToken != null)
            return "chooseOperationAdmin";
        return "document-update";
    }

    @PostMapping("/update")
    public String postUpdate(@RequestParam ("db_name") String db_name,
                             @RequestParam ("collection_name") String collection_name,
                             @RequestParam("id") String id,
                             @RequestParam("prop") String prop ,
                             @RequestParam("value") String value,
                             HttpSession session , Model model){
        type="admin";
        UserToken userToken = (UserToken) session.getAttribute("entered");
        if (userToken != null)
            return "chooseOperationAdmin";
        if(!collectionService.isExist(db_name,collection_name,session) ||
                !databaseService.isExist(db_name,session)){
            model.addAttribute("result","no database or collection");
            return "responsePage";
        }
        String response = documentService.updateDocument(db_name, collection_name, id, prop, value, session);
        model.addAttribute("result",response);
        return "responsePage";
    }
    @GetMapping("/addDepartment")
    public String addDepartmentGet(){
        return "departmentInput";
    }
    @PostMapping("/addDepartment")
    public String addDepartmentPost(@RequestParam ("building_name") String buildingName,
                                    @RequestParam ("department_id") String id,
                                    @RequestParam ("room_number") String roomNum,
                                    @RequestParam("bathroom_number") String bathRoomNum,
                                    HttpSession session,Model model){

            boolean flag = documentService.addDepartment(buildingName, id, roomNum, bathRoomNum, session);
            if (flag) {
                model.addAttribute("result", "Department Added Successfully");
            } else model.addAttribute("result", "Department Adding Faild");

        return "responsePage";
    }
    @GetMapping("/addStore")
    public String addStore(){
        return "storeInput";
    }

    @PostMapping("/addStore")
    public String addStorePost(@RequestParam ("building_name") String building,
                                    @RequestParam ("store_id") String id,
                                    @RequestParam ("area") String area,
                                    @RequestParam("height") String height,
                                    HttpSession session,Model model){

        boolean flag = documentService.addStore(building,id, area, height, session);
        if (flag) {
            model.addAttribute("result", "Store Added Successfully");
        } else model.addAttribute("result", "Store Adding Faild");

        return "responsePage";
    }


}
