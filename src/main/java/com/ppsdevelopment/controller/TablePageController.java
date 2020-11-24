package com.ppsdevelopment.controller;

import com.ppsdevelopment.domain.ETable;
import com.ppsdevelopment.tools.StringHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("table")
public class TablePageController {
    private int counter = 4;
    private ETable table=ETable.getInstance();
/*
    private List<Map<Integer, String>> messages = new LinkedList<Map<Integer, String>>();
     {
        int counter=1;
        for (int i=0;i<3;i++){
            HashMap map=new HashMap();
            for (int y=0;y<5;y++){
                map.put(y,Integer.toString(counter++));
            }
            messages.add(map);

        }
    }
*/

    @GetMapping
    public String index(Map<String, Object> model){
        model.put("tableitems", StringHelper.getTableItems(table));
        return "tablepage";
    }

}
