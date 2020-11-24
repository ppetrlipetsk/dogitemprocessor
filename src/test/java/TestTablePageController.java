import com.ppsdevelopment.controller.TablePageController;
import com.ppsdevelopment.tools.StringHelper;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;

public class TestTablePageController {
    private List<Map<Integer, String>> messages = new LinkedList<Map<Integer, String>>();
    {
        //messages = new LinkedList<Map<Integer, String>>()
        int counter=1;
        for (int i=0;i<3;i++){
            HashMap map=new HashMap();
            for (int y=0;y<5;y++){
                map.put(y,Integer.toString(counter++));
            }
            messages.add(map);
        }
    }

    @Test
    public void testArrayMaker(){
        String s=StringHelper.getTableItems(messages);
        assertTrue(s.length()>0);
    }
}
