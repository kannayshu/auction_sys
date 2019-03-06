package service;

import com.auction_sys.pojo.Product;
import com.auction_sys.pojo.User;
import com.auction_sys.service.ProductService;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.TestBase;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created By liuda on 2018/6/3
 */
public class ProductServiceTest extends TestBase{
    ObjectMapper objectMapper = new ObjectMapper();
    JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
            .createJsonGenerator(System.out, JsonEncoding.UTF8);

    @Autowired
    ProductService productService;

    public ProductServiceTest() throws IOException {
    }

    @Test
    public void test() throws JsonProcessingException {
        Product productVO = new Product();
        productVO.setIsDeleted((byte)0);
        productVO.setCategoryId(5);
        productVO.setCurrentPrice(new BigDecimal(100));
        productVO.setDetail("detial");
        productVO.setMainImage("main");
        productVO.setName("name");
        productVO.setReservePrice(new BigDecimal(100));
        productVO.setScale(new BigDecimal(100));
        productVO.setStartingPrice(new BigDecimal(100));
        productVO.setSubTitle("title");
        productVO.setSellerId(1L);
        productVO.setStatus((byte)1);
        productVO.setSubImage("sub");
        User user = new User();
        user.setUserId(1L);
        String userJsonStr = objectMapper.writeValueAsString(productService.addProduct(productVO,user));
        System.out.println(userJsonStr);
        Product productVO1 = new Product();
        productVO.setProductId(1L);
        productVO.setSellerId(1L);
        productVO.setStatus((byte)2);
        userJsonStr = objectMapper.writeValueAsString(productService.updateProduct(productVO,user));
        System.out.println(userJsonStr);
        userJsonStr = objectMapper.writeValueAsString(productService.selectProduct(1L));
        System.out.println(userJsonStr);
    }
}
