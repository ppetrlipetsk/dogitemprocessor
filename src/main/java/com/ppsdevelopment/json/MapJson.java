package com.ppsdevelopment.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class MapJson {
    public static Map<String,Object> map(String value) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map empMap = objectMapper.readValue(value,Map.class);
        return empMap;
    }

    public static JsonNode get(String name, String value) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root=objectMapper.readTree(value);
        return root.path(name);
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
