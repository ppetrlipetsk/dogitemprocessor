package com.ppsdevelopment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ppsdevelopment.datalib.TableCellRequest;
import com.ppsdevelopment.domain.reserv.CellClass;
import com.ppsdevelopment.domain.reserv.ETable;
import com.ppsdevelopment.envinronment.Pagination;
import com.ppsdevelopment.json.MapJson;
import com.ppsdevelopment.service.SourceTableImpl;
import com.ppsdevelopment.tmctypeslib.DetectType;
import com.ppsdevelopment.tmctypeslib.FieldType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("tablerest")
public class RESTController {

    private SourceTableImpl sourceTable;
    Pagination pagination;

    @PostMapping("/setitems")
    public @ResponseBody String setItems(@RequestBody Map<String, String> message){
        int x=Integer.parseInt(message.get("x"));
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

    @PostMapping(value="/setcell", consumes = "application/json;charset=UTF-8")
    public @ResponseBody String setCell(@RequestBody TableCellRequest data){
        String fieldName=sourceTable.getAliases().get(data.getFieldIndex()-1).getFieldalias();
        String fieldType=sourceTable.getAliases().get(data.getFieldIndex()-1).getFieldtype();

        if (DetectType.isValueValid(FieldType.valueOf(fieldType),data.getValue())){
            sourceTable.updateFieldValue(Long.valueOf(data.getId()),data.getValue(), fieldName, FieldType.valueOf(fieldType));
        }

        return data.toString();
    }

    @PostMapping("/setcellw")
    public @ResponseBody String setCell(@RequestParam(required = false, defaultValue = "-1") String i1,
                                        @RequestParam(required = false, defaultValue = "-1") String i2,
                                        @RequestParam(required = false,defaultValue = "-1") String i3) {
        return i1+i2+i3;
    }

    @PostMapping("/setpage")
    public @ResponseBody String setPage(@RequestBody String s, HttpServletRequest request) {
        System.out.println(request);
        Integer pageNumber;
        try {

            pageNumber=MapJson.get("pagenumber", s).asInt();
            pagination.setCurrentPage(pageNumber);
            String tableData=sourceTable.getResultAsArrayLine(sourceTable.getAll());
            return sourceTable.getPaginationJsonResponse(tableData);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sourceTable.getPaginationJsonResponse(s);
    }


    @PostMapping("/setpageblock")
    public @ResponseBody String setPageBlock(@RequestBody String s, HttpServletRequest request) {
        Integer pageNumber;
        Integer firstPage;
        try {
            pageNumber=MapJson.get("pagenumber", s).asInt();
            firstPage=MapJson.get("firstpage", s).asInt();
            pagination.setCurrentPage(pageNumber);
            pagination.setFirstPage(firstPage);
            String tableData=sourceTable.getResultAsArrayLine(sourceTable.getAll());
            return sourceTable.getPaginationJsonResponse(tableData);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sourceTable.getPaginationJsonResponse(s);
    }

    @PostMapping("/sortmaintable")
    public  @ResponseBody String sortMainPage(@RequestBody String s){
        Integer columnnumber;
        try {
            columnnumber=MapJson.get("columnnumber", s).asInt();
            String columnName=pagination.getSortColumnName();
            if (columnName.equals(sourceTable.getAliases().get(columnnumber-1).getFieldalias())){
                pagination.setSortDirection(!pagination.isSortDirection());
                pagination.setCurrentPage(1);
                pagination.setFirstPage(1);
            }
            else{
                pagination.setSortColumnName(sourceTable.getAliases().get(columnnumber-1).getFieldalias());
                pagination.setSortDirection(true);
                pagination.setCurrentPage(1);
                pagination.setFirstPage(1);
            }
            String tableData=sourceTable.getResultAsArrayLine(sourceTable.getAll());
            return sourceTable.getPaginationJsonResponse(tableData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sourceTable.getPaginationJsonResponse(s);
    }


    @GetMapping
    public String getItems(){
        return "main";
    }


    @Autowired
    public void setSourceTable(@Qualifier("SourceTableImpl") SourceTableImpl sourceTable) {
        this.sourceTable = sourceTable;
    }

    @Autowired
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

}
