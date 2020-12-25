package com.ppsdevelopment.viewlib.dataprepare.datalib;

import com.google.gson.Gson;
import com.ppsdevelopment.SqlQueryPreparer;
import com.ppsdevelopment.converters.DateFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collector;

public class DataAdapter {
    public static String getResultAsArrayLine(List lines){
        String s="["+lines.stream().collect(getCollector(DataAdapter::getQueryValues)).toString()+"]";
        return s;
    }

    public static String getResultAsJSONLine(List lines){
        Object[] items=lines.toArray();
        Gson gson = new Gson();
        String value=gson.toJson(items);
        return value;
    }

    private static String getJSONQueryValues(Object items) {
        Gson gson = new Gson();
        String value=gson.toJson(items);
        return value;
    }


    private static String getQueryValues(Object item) {
        List<Object> lines= Arrays.asList(((Object[]) item));
        String fieldsLine = lines
                .stream()
                .collect(getCollector(DataAdapter::getFieldValueAsString));//.toString();
        return "["+fieldsLine+"]";
    }

    private static Collector<Object, StringJoiner, String> getCollector(Function<Object,String> action){
        return Collector.of(
                () -> new StringJoiner(" , "),          // supplier
                (j, p) -> j.add(action.apply(p)),               // accumulator
                StringJoiner::merge,                            // combiner // (j1, j2) -> j1.merge(j2),               // combiner
                StringJoiner::toString);                        // finisher
    }

    private static String getFieldValueAsString(Object field) {
        try {
            if (field.toString().contains("\"")){
                System.out.println(field.toString());
            }

            return SqlQueryPreparer.getCaptionFromValue(field, DateFormatter.INTERNATIONAL_DATE_FORMAT,"/","\"",true);
        } catch (Exception e) {
            return "SQL query values generating error!!!";
        }
    }
}
