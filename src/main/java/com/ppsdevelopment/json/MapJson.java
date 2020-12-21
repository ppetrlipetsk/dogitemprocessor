package com.ppsdevelopment.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class MapJson {
    public static Map<String,Object> map(String value) {

        ObjectMapper objectMapper = new ObjectMapper();
        Map empMap = null;
        try {
            empMap = objectMapper.readValue(value, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return empMap;
    }

    public static JsonNode get(String name, String value) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root= null;
        try {
            root = objectMapper.readTree(value);
            return root.path(name);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}

/*
    ObjectMapper objectMapper = new ObjectMapper();
        try {
                JsonNode root=objectMapper.readTree(s);
                Iterator<JsonNode> elements = root.elements();
        while(elements.hasNext()){
        JsonNode n=elements.next();
        System.out.println(n.asLong());
        }
        System.out.println(root.path("pagenumber").asLong());
        } catch (JsonProcessingException e) {
        e.printStackTrace();
        }
*/
