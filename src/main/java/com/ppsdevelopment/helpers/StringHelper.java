package com.ppsdevelopment.helpers;

import org.springframework.web.util.HtmlUtils;

import java.util.List;

public class StringHelper {
    public static String getTableItems(List<List<String>> list) {
        String s="[";
        int counter=0;
        for (List<String>map: list) {
            if (counter>0) s+=",";
            s+="[";
            if (s.substring(s.length()-1).equals("]")) s+=",";
            int counter2=0;
            for (String cell: map){
                if(counter2>0) s+=",";
                s+="'"+ escapeValue(cell)+"'";
                counter2++;
            }
            s+="]";
            counter++;
        }
        s+="]";
        return s;
    }

    private static String escapeValue(String cell) {
        return cell.replace("\\","\\\\").replace("\"","\\\"").replace("'","\\'").replace("/","\\/");
    }

}
