package com.ppsdevelopment.tools;

import java.util.List;
import java.util.Map;

public class StringHelper {
    public static String getTableItems(List<Map<Integer,String>> list) {
        String s="[";
        for (Map<Integer, String> map: list) {
            if (s.substring(s.length()-1).equals("]")) s+=",";
            s+="[";
            if (s.substring(s.length()-1).equals("]")) s+=",";
            for (Map.Entry<Integer, String> entry: map.entrySet()){
                if (!s.substring(s.length()-1).equals("[")) s+=",";
                s+=entry.getValue();
            }
            s+="]";
        }
        s+="]";
        return s;
    }

}
