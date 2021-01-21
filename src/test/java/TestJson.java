import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ppsdevelopment.service.viewservices.Pagination;
import org.junit.Test;

import java.util.HashMap;

public class TestJson {
    @Test
    public void createJson(){
        Gson gson = new Gson();

        Pagination pagination=new Pagination();
        String json = gson.toJson(pagination);
        System.out.println(json);
        pagination.setCurrentPage(11);
        System.out.println(pagination.toValueString());
        pagination= gson.fromJson(json, Pagination.class);

        System.out.println((gson.fromJson(json, HashMap.class)).get("recordsCount"));
        System.out.println(pagination.toValueString());
        JsonElement e= gson.toJsonTree(json);
        System.out.println(e);

    }

    @Test
    public void JSON1(){
        ObjectMapper mapper = new ObjectMapper();
    }
}
