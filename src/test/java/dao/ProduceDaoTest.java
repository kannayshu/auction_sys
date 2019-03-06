package dao;

import com.auction_sys.vo.ProductVO;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

/**
 * Created By liuda on 2018/6/3
 */
public class ProduceDaoTest {
    ObjectMapper objectMapper = new ObjectMapper();
    JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
            .createJsonGenerator(System.out, JsonEncoding.UTF8);

    public ProduceDaoTest() throws IOException {
    }

    @Test
    public void insert() throws IOException {
        JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
                .createJsonGenerator(System.out, JsonEncoding.UTF8);
        ProductVO productVO = null;
        String userJsonStr = objectMapper.writeValueAsString(productVO);
        System.out.println(userJsonStr);
    }

}
