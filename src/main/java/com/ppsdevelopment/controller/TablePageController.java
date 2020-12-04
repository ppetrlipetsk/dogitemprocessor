package com.ppsdevelopment.controller;

import com.ppsdevelopment.domain.Aliases;
import com.ppsdevelopment.domain.reserv.ETable;
import com.ppsdevelopment.domain.Tables;
import com.ppsdevelopment.repos.AliasesRepo;
import com.ppsdevelopment.repos.TablesRepo;
import com.ppsdevelopment.helpers.StringHelper;
import com.ppsdevelopment.service.TablesService;
import com.ppsdevelopment.service.res.ExTableDAOImpl;
import com.ppsdevelopment.service.SourceTableImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    AliasesRepo aliasesRepo;


    @Autowired
    ExTableDAOImpl exTableDAO;


    private SourceTableImpl sourceTable;

    @Autowired
    TablesService tablesService;



    @GetMapping
    public String index(Map<String, Object> model){
        model.put("tableitems", StringHelper.getTableItems(ETable.getCells()));

        List<Tables> table=tablesRepo.findByTablename("zmm2021");
        List<Aliases> aliases=aliasesRepo.getAllByTable(table.get(0).getId());

        String tableHeader= sourceTable.getHeaderDataList(aliases);
        model.put("headervalues",tableHeader);

        String tableData=sourceTable.getFieldsValuesLine(aliases);
        model.put("tabledata",tableData);

        return "tablepage";

    }

    public SourceTableImpl getSourceTable() {
        return sourceTable;
    }

    @Autowired
    public void setSourceTable(@Qualifier("SourceTableImpl") SourceTableImpl xTable) {
        this.sourceTable = xTable;
    }

}
