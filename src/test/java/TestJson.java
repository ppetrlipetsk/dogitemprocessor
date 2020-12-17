import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.ppsdevelopment.envinronment.Pagination;
import org.junit.Test;
import org.springframework.security.core.parameters.P;

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

        System.out.println(pagination.toValueString());
    }
}
