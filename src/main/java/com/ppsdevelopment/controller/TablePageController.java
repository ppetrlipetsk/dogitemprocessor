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
    {
        ETable.getInstance();
    }

    @GetMapping
    public String index(Map<String, Object> model){
        model.put("tableitems", StringHelper.getTableItems(ETable.getCells()));
        //model.put("tableitems", ETable.getCells());
        return "tablepage";
    }

}
