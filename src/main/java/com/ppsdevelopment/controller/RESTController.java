package com.ppsdevelopment.controller;

import com.ppsdevelopment.domain.CellClass;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("tablerest")
public class RESTController {

    @PostMapping("/setitems")
    public @ResponseBody String setItems(@RequestBody Map<String, String> message){
        //System.out.println(message.toString());
        //return "message="+value+x.toString()+y.toString();
      //  System.out.println("x="+message.containsKey("x"));
        return "OK"+message.toString();
    }

    @PostMapping("/setitem")
    public @ResponseBody String setItem(@RequestBody CellClass cellClass){
        //System.out.println(message.toString());
        return "cell="+cellClass.getValue();
    }

    @GetMapping
    public String getItems(){
        //System.out.println(message.toString());

        return "main";
    }

}
