package com.example.demo.Controller;


import com.example.demo.Model.UserToken;
import com.example.demo.Service.CollectionService;
import com.example.demo.Service.DatabaseService;
import com.example.demo.Service.RecordService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/record")
public class RecordController {
    @Autowired
    private CollectionService collectionService;

    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private RecordService recordService;
    @Autowired
    private HttpSession session;

    @PostMapping("/listOfDepartment")
    public String ListOfDepartment(@RequestParam("db_name") String db_name,
                           HttpSession session , Model model){
        UserToken userToken = (UserToken) session.getAttribute("entered");
        String list = recordService.getList(db_name,"department",session);
        model.addAttribute("result",list);
        return "responsePage";
    }
    @PostMapping("/listOfStore")
    public String ListOfStore(@RequestParam("db_name") String db_name,
                              HttpSession session , Model model){
        UserToken userToken = (UserToken) session.getAttribute("entered");
        String list = recordService.getList(db_name,"store",session);
        model.addAttribute("result",list);
        return "responsePage";
    }
    @GetMapping("/add")
    public String addRecord(){
        UserToken userToken = (UserToken) session.getAttribute("entered");
        return "document";
    }


    @GetMapping("/delete")
    public String getDelete(){
        UserToken userToken = (UserToken) session.getAttribute("entered");
        return "document-delete";
    }

    @PostMapping("/delete")
    public String postDelete(@RequestParam ("building_name") String db_name,
                             @RequestParam ("permission") String collection_name,
                             @RequestParam("id") String id,
                             HttpSession session , Model model){
        UserToken userToken = (UserToken) session.getAttribute("entered");
        String response = recordService.deleteRecord(db_name,collection_name, id ,session);
        model.addAttribute("result",response);
        return "responsePage";
    }

    @GetMapping("/update")
    public String getUpdate(){
        UserToken userToken = (UserToken) session.getAttribute("entered");

        return "document-update";
    }

    @PostMapping("/update")
    public String postUpdate(@RequestParam ("db_name") String db_name,
                             @RequestParam ("collection_name") String collection_name,
                             @RequestParam("id") String id,
                             @RequestParam("prop") String prop ,
                             @RequestParam("value") String value,
                             HttpSession session , Model model){
        UserToken userToken = (UserToken) session.getAttribute("entered");
        String response = recordService.updateRecord(db_name, collection_name, id, prop, value, session);
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

        String response = recordService.addDepartment(buildingName, id, roomNum, bathRoomNum, session);
           model.addAttribute("result", response);

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

        String response = recordService.addStore(building,id, area, height, session);
        model.addAttribute("result", response);

        return "responsePage";
    }


}
