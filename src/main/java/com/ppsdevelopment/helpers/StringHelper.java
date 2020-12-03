package com.ppsdevelopment.helpers;

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
                s+="'"+cell+"'";
                counter2++;
            }
            s+="]";
            counter++;
        }
        s+="]";
        return s;
    }

}
