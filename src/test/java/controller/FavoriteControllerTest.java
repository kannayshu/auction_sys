package controller;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.controller.FavoriteController;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.TestBase;

import java.io.IOException;

/**
 * Created By liuda on 2018/7/24
 */
public class FavoriteControllerTest extends TestBase{

    @Autowired
    FavoriteController favoriteController;

    @Test
    public void getUserFavorite() throws IOException {
        login();
        ServerResponse serverResponse = favoriteController.getUserFavorite(1,10,session);
        JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
                .createJsonGenerator(System.out, JsonEncoding.UTF8);

        String serJsonStr = objectMapper.writeValueAsString(serverResponse);
        System.out.println(serJsonStr);
    }

    @Test
    public void addFavoriteProduct() throws IOException {
        login();
        ServerResponse serverResponse = favoriteController.addFavoriteProduct(22l,session);
        JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
                .createJsonGenerator(System.out, JsonEncoding.UTF8);

        String serJsonStr = objectMapper.writeValueAsString(serverResponse);
        System.out.println(serJsonStr);
    }

    @Test
    public void removeFavoriteProduct() throws IOException {
        login();
        ServerResponse serverResponse = favoriteController.removeFavoriteProduct(20l,session);
        JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
                .createJsonGenerator(System.out, JsonEncoding.UTF8);

        String serJsonStr = objectMapper.writeValueAsString(serverResponse);
        System.out.println(serJsonStr);
    }
}
