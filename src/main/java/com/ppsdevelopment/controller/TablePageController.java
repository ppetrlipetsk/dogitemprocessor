package com.ppsdevelopment.controller;

import com.ppsdevelopment.domain.ETable;
import com.ppsdevelopment.domain.Tables;
import com.ppsdevelopment.repos.TablesRepo;
import com.ppsdevelopment.tools.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private TablesRepo tablesRepo;

    @GetMapping
    public String index(Map<String, Object> model){
        model.put("tableitems", StringHelper.getTableItems(ETable.getCells()));
        Iterable<Tables> tables=tablesRepo.findAll();
        //model.put("tableitems", ETable.getCells());
        model.put("tables",tables);
        return "tablepage";
    }

}
