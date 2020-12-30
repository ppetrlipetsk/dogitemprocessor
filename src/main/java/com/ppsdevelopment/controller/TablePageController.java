package com.ppsdevelopment.controller;

import com.ppsdevelopment.domain.User;
import com.ppsdevelopment.envinronment.*;
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
import java.util.Map;

@Controller
@RequestMapping("table")
public class TablePageController {

    private HttpSession session;
    private HttpServletRequest request;
    private SourceTableImpl sourceTable;
    private SettingsProvider settingsProvider;


    @ModelAttribute("browser")
    public void getBrowser(Model model){
        model.addAttribute("browser", HeaderParser.getBrowserName(request.getHeader("User-Agent")).contains("IE"));
    }

    @GetMapping
    public String index(Map<String, Object> model) throws Exception {
   //     User user= Credentials.getUser();
        Pagination pagination= (Pagination) settingsProvider.getSettingsValues(sourceTable.getPaginationName(), Pagination.class);

        if (pagination==null) {
            pagination = new Pagination();
            pagination.setSortColumnName("id");
            pagination.setSortDirection(true);
        }
        String tableHeader= sourceTable.getTableHeader();
        model.put("headervalues",tableHeader);


        String tableData=sourceTable.getResultAsStringLine(sourceTable.getAll());
        model.put("tabledata",tableData);

        pagination.setRecordsCount(sourceTable.getCount());
        model.put("pagination",pagination);

        session.setAttribute("pagination",pagination);
        settingsProvider.setSettingsValue(sourceTable.getPaginationName(),pagination);
        return "tablepage";
    }

    @Autowired
    public void setSourceTable(@Qualifier("SourceTableImpl") SourceTableImpl xTable) {
        this.sourceTable = xTable;
    }

    @Autowired
    public void setSession(HttpSession session) {
        this.session = session;
    }

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired

    public void setSettingsProvider(SettingsProvider settingsProvider) {
        this.settingsProvider = settingsProvider;
    }
}
