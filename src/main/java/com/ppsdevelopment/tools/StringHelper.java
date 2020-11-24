package com.ppsdevelopment.tools;

import com.ppsdevelopment.domain.ETable;

import java.util.List;
import java.util.Map;

public class StringHelper {
    public static String getTableItems(ETable table) {
        List<List<String>> list=table.getCells();
        String s="[";
        for (List<String>map: list) {
            if (s.substring(s.length()-1).equals("]")) s+=",";
            s+="[";
            if (s.substring(s.length()-1).equals("]")) s+=",";

            for (String cell: map){
                if (!s.substring(s.length()-1).equals("[")) s+=",";
                s+=cell;
            }

            s+="]";
        }
        s+="]";
        return s;
    }

}
