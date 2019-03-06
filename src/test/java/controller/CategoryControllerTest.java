package controller;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.controller.CategoryController;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.TestBase;

import java.io.IOException;

/**
 * Created By liuda on 2018/7/14
 */
public class CategoryControllerTest extends TestBase {
    @Autowired
    CategoryController categoryController;

    @Test
    public void getParentNodeList() throws IOException {
        ServerResponse serverResponse= categoryController.getParentsNode(100101);
        JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
                .createJsonGenerator(System.out, JsonEncoding.UTF8);

        String serJsonStr = objectMapper.writeValueAsString(serverResponse);
        System.out.println(serJsonStr);
    }

    @Test
    public void getNextLevelNode() throws IOException {
        ServerResponse serverResponse= categoryController.getNextLevelNode(100005);
        JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
                .createJsonGenerator(System.out, JsonEncoding.UTF8);

        String serJsonStr = objectMapper.writeValueAsString(serverResponse);
        System.out.println(serJsonStr);
    }

    @Test
    public void getSameLevelNode() throws IOException {
        ServerResponse serverResponse= categoryController.getSameLevelNode(100025);
        JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
                .createJsonGenerator(System.out, JsonEncoding.UTF8);

        String serJsonStr = objectMapper.writeValueAsString(serverResponse);
        System.out.println(serJsonStr);
    }
}
