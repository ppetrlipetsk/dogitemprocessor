package com.ppsdevelopment.domain;
import java.util.LinkedList;
import java.util.List;

public class ETable {
    private static List<List<String>> cells = new LinkedList<>();
    private static ETable instance;

    private ETable()
    {
        int counter=1;
        for (int i=0;i<3;i++){
            List<String> lines=new LinkedList<>();
            for (int y=0;y<5;y++){
                lines.add(Integer.toString(counter++));
            }
            cells.add(lines);
        }
    }

    public static ETable getInstance(){
        if (instance==null)
            instance=new ETable();
        return instance;
    }

    public static List<List<String>>  getCells(){
        return cells;
    }

}
