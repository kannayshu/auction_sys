package service;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.pojo.Category;
import com.auction_sys.service.CategoryService;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import test.TestBase;

import java.io.IOException;
import java.util.List;

/**
 * Created By liuda on 2018/5/29
 */
public class CategoryServiceTest extends TestBase{
    @Autowired
    CategoryService categoryService;
    @Autowired

    ObjectMapper objectMapper = new ObjectMapper();
    JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
            .createJsonGenerator(System.out, JsonEncoding.UTF8);

    public CategoryServiceTest() throws IOException {
    }



    @Test
    public void test() throws IOException {
        Category category = new Category(null,0,"l1_7");
        System.out.println();
        String userJsonStr = objectMapper.writeValueAsString(categoryService.getPrimaryNode());
        System.out.println(userJsonStr);
        userJsonStr = objectMapper.writeValueAsString(categoryService.getSameLevelNode(3));
        System.out.println(userJsonStr);
        objectMapper.writeValue(System.out, categoryService.getNextLevelNode(1));
        System.out.println();
        objectMapper.writeValue(System.out, categoryService.getParentsNode(6));
        System.out.println();
        category = new Category(null,null,"11_7()");
        objectMapper.writeValue(System.out, categoryService.addNode(category));
        System.out.println();
        objectMapper.writeValue(System.out, categoryService.deleteNode(7));
    }
    @Test
    public void testCommon(){
    }
}
