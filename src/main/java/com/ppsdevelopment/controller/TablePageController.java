package com.ppsdevelopment.controller;

import com.ppsdevelopment.domain.reserv.ETable;
import com.ppsdevelopment.helpers.StringHelper;
import com.ppsdevelopment.service.SourceTableImpl;
import com.ppsdevelopment.service.res.ExTableDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("table")
public class TablePageController {
    private int counter = 4;
    {
        ETable.getInstance();
    }

/*
    @Autowired
    private TablesRepo tablesRepo;
*/

    /*@Autowired
    AliasesRepo aliasesRepo;
*/
    @Autowired
    HttpSession session;

    @Autowired
    ExTableDAOImpl exTableDAO;


    @Qualifier("SourceTableImpl")
    @Autowired
    private SourceTableImpl sourceTable;

/*

    @Autowired
    TablesService tablesService;
*/



    @GetMapping
    public String index(Map<String, Object> model){
        model.put("tableitems", StringHelper.getTableItems(ETable.getCells()));

//        List<Tables> table=tablesRepo.findByTablename("zmm2021");
//        List<Aliases> aliases=aliasesRepo.getAllByTable(table.get(0).getId());

        String tableHeader= sourceTable.getHeaderDataList();
        model.put("headervalues",tableHeader);

        String tableData=sourceTable.getFieldsValuesLine();
        model.put("tabledata",tableData);

        String checkTypes=sourceTable.getCheckTypesLine();
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
