package com.ppsdevelopment.service.databasetableimpl.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ppsdevelopment.SqlQueryPreparer;
import com.ppsdevelopment.converters.DateFormatter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;

public class DataAdapter {
    private static final String DATAPATTERN="dd.MM.yyyy";

    public static Object valueFromJson(String key, String jsonValue){
        Gson gson = new Gson();
        Map values=gson.fromJson(jsonValue, HashMap.class);
        if ((values!=null)&&(values.containsKey(key))) return  values.get(key);
        else
            return null;
    }

    public static Object objectFromJson(String jsonValue, Class c){
        Gson gson = new Gson();
        Object values=gson.fromJson(jsonValue, c);
        return values;
    }


    public static String asJSON(List lines){
        return newJSonInstance().toJson(lines);
    }

    public static String getListAsJSONLine(List lines){
        return newJSonInstance().toJson(lines.toArray());
    }

    private static Gson newJSonInstance(){
        GsonBuilder builder=new GsonBuilder();
        builder.setDateFormat(DATAPATTERN);
        return builder.create();
    }

    // Оставлен для примера Stream
    private static String valueAsJSON(List lines){
        String s="["+lines.stream().collect(getCollector(DataAdapter::getQueryValues)).toString()+"]";
        return s;
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

    // Оставлено для примера
    public static JsonNode getValueFromJson(String name, String value) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root;
        try {
            root = objectMapper.readTree(value);
            return root.path(name);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


}
