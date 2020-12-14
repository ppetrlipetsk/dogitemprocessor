package com.ppsdevelopment.controller;

import com.ppsdevelopment.envinronment.Pagination;
import com.ppsdevelopment.service.SourceTableImpl;
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

    @Autowired
    HttpSession session;

    private Pagination pagination;
    private SourceTableImpl sourceTable;


    @GetMapping
    public String index(Map<String, Object> model){

        pagination.setSortColumnName("id");
        pagination.setSortDirection(true);

        String tableHeader= sourceTable.getTableHeader();
        model.put("headervalues",tableHeader);


        String tableData=sourceTable.getResultAsArrayLine(sourceTable.getAll());
        model.put("tabledata",tableData);

        pagination.setRecordsCount(sourceTable.getCount());
        model.put("pagination",pagination);

        return "tablepage";
    }

    @Autowired
    public void setSourceTable(@Qualifier("SourceTableImpl") SourceTableImpl xTable) {
        this.sourceTable = xTable;
    }

    @Autowired
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

}
