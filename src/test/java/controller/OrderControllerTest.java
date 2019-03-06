package controller;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.controller.OrderController;
import com.auction_sys.controller.SellerOrderController;
import com.auction_sys.controller.UserController;
import com.auction_sys.dao.ProductOrderMapper;
import com.auction_sys.pojo.DepositOrder;
import com.auction_sys.pojo.User;
import com.auction_sys.service.OrderService;
import com.auction_sys.vo.ProductVO;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import test.TestBase;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created By liuda on 2018/7/10
 */
public class OrderControllerTest extends TestBase{
    @Autowired
    UserController userController;
    @Autowired
    OrderController orderController;
    @Autowired
    SellerOrderController sellerOrderController;
    @Autowired
    ProductOrderMapper productOrderMapper;


    @Test
    public void testBid() throws IOException {
        login();
    }
    @Test
    public void testDepositPay() throws IOException {
        login();
        ServerResponse serverResponse = orderController.depositPay(session,15314777467150l);
        JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
                .createJsonGenerator(System.out, JsonEncoding.UTF8);

        String serJsonStr = objectMapper.writeValueAsString(serverResponse);
        System.out.println(serJsonStr);

    }

    @Test
    public void testDepositRefund() throws IOException {
        login();
        ServerResponse serverResponse = orderController.depositRefund(session,15313761040240l);
        JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
                .createJsonGenerator(System.out, JsonEncoding.UTF8);

        String serJsonStr = objectMapper.writeValueAsString(serverResponse);
        System.out.println(serJsonStr);

    }

    @Test
    public void testProductOrderPreOrder() throws IOException {
        login();
        ServerResponse serverResponse = orderController.productOrderPay(session,15313761040241l);
        JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
                .createJsonGenerator(System.out, JsonEncoding.UTF8);

        String serJsonStr = objectMapper.writeValueAsString(serverResponse);
        System.out.println(serJsonStr);
    }

    @Test
    public void testRreateDepositOrderForSeller() throws IOException {
        login();
        ServerResponse serverResponse = sellerOrderController.createDepositOrderForSeller(session,29l);
        JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
                .createJsonGenerator(System.out, JsonEncoding.UTF8);

        String serJsonStr = objectMapper.writeValueAsString(serverResponse);
        System.out.println(serJsonStr);
       if (serverResponse.isSuccess()){
            Long orderId = (Long) serverResponse.getData();

           serverResponse = sellerOrderController.depositPay(session,orderId);
           serJsonStr = objectMapper.writeValueAsString(serverResponse);
           System.out.println(serJsonStr);
        }
    }

    @Test
    public void testSellerDepositPay() throws IOException {
        login();
        ServerResponse serverResponse = sellerOrderController.depositPay(session,15321985045260l);
        JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
                .createJsonGenerator(System.out, JsonEncoding.UTF8);
        String serJsonStr = objectMapper.writeValueAsString(serverResponse);
        System.out.println(serJsonStr);
    }

    @Test
    public void testSellerDeliverGoods() throws IOException {
        login();
        ServerResponse serverResponse = sellerOrderController.deliverGoods(session,32l,"12315","lala");
        String serJsonStr = objectMapper.writeValueAsString(serverResponse);
        System.out.println(serJsonStr);
    }

}
