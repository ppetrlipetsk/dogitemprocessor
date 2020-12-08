package com.ppsdevelopment.controller;

import com.ppsdevelopment.datalib.TableCellRequest;
import com.ppsdevelopment.domain.reserv.CellClass;
import com.ppsdevelopment.domain.reserv.ETable;
import com.ppsdevelopment.service.SourceTableImpl;
import com.ppsdevelopment.tmctypeslib.FieldType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("tablerest")
public class RESTController {

    @Qualifier("SourceTableImpl")
    @Autowired
    private SourceTableImpl sourceTable;



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

    @PostMapping(value="/setcell", consumes = "application/json;charset=UTF-8")
    public @ResponseBody String setCell(@RequestBody TableCellRequest data){
        String fieldName=sourceTable.getAliases().get(data.getFieldIndex()-1).getFieldalias();
        String fieldType=sourceTable.getAliases().get(data.getFieldIndex()-1).getFieldtype();
        if (sourceTable.isDataValid(data.getValue(), fieldType)){
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

    @GetMapping
    public String getItems(){
        //System.out.println(message.toString());

        return "main";
    }

}
