package com.ppsdevelopment.controller;

import com.ppsdevelopment.envinronment.HeaderParser;
import com.ppsdevelopment.envinronment.Pagination;
import com.ppsdevelopment.service.SourceTableImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("table")
public class TablePageController {

    @Autowired
    HttpSession session;
    @Autowired
    HttpServletRequest request;

    //private Pagination pagination;
    private SourceTableImpl sourceTable;

    @ModelAttribute("browser")
    public void getBrowser(Model model){
        model.addAttribute("browser", HeaderParser.getBrowserName(request.getHeader("User-Agent")).contains("IE"));
    }

    @GetMapping
    public String index(Map<String, Object> model){
        Object attr1=session.getAttribute("attr1");
        Pagination pagination=(Pagination) session.getAttribute("pagination");
        if (pagination==null){
            pagination=new Pagination();
        }

        if (attr1==null){
            String attr2="attr1="+new Date().toString();
            session.setAttribute("attr1",attr2);
        }

        pagination.setSortColumnName("id");
        pagination.setSortDirection(true);

        String tableHeader= sourceTable.getTableHeader();
        model.put("headervalues",tableHeader);


        String tableData=sourceTable.getResultAsArrayLine(sourceTable.getAll(pagination));
        model.put("tabledata",tableData);

        pagination.setRecordsCount(sourceTable.getCount());
        model.put("pagination",pagination);

        model.put("attr1",session.getAttribute("attr1").toString());

        session.setAttribute("pagination",pagination);
        return "tablepage";
    }

    @Autowired
    public void setSourceTable(@Qualifier("SourceTableImpl") SourceTableImpl xTable) {
        this.sourceTable = xTable;
    }

//    @Autowired
    //public void setPagination(Pagination pagination) {
        //this.pagination = pagination;
    //}

}
