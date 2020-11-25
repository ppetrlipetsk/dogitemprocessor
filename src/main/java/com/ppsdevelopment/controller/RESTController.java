package com.ppsdevelopment.controller;

import com.ppsdevelopment.domain.CellClass;
import com.ppsdevelopment.domain.ETable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("tablerest")
public class RESTController {

    @PostMapping("/setitems")
    public @ResponseBody String setItems(@RequestBody Map<String, String> message){
        int x=Integer.valueOf(message.get("x"));
        int y=Integer.valueOf(message.get("y"));
        String value=message.get("value");
        List<String> list= ETable.getCells().get(y);
        list.set(x,value);
        return "OK"+message.toString()+ETable.getCells().toString();
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
