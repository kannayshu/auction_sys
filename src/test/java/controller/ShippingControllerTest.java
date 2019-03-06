package controller;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.controller.ShippingController;
import com.auction_sys.pojo.Shipping;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.TestBase;

import java.io.IOException;

/**
 * Created By liuda on 2018/7/19
 */
public class ShippingControllerTest extends TestBase{

    @Autowired
    ShippingController shippingController;


    @Test
    public void testAddShipping() throws IOException {
        login();
        Shipping shipping = new Shipping();
        shipping.setReceiverPhone("87610823");
        shipping.setReceiverProvince("广东");
        shipping.setReceiverCity("广州");
        shipping.setReceiverDistrict("谷饶");
        shipping.setReceiverName("刘大");
        shipping.setReceiverZip("515159");
        shipping.setReceiverAddress("大同");
        ServerResponse serverResponse = shippingController.addShipping(shipping,session);
        printServerRespon(serverResponse);
    }

    @Test
    public void testUpdateShipping() throws IOException {
        login();
        Shipping shipping = new Shipping();
        shipping.setShippingId(1l);
        shipping.setReceiverZip("515100");
        ServerResponse serverResponse = shippingController.updateShipping(shipping,session);
        printServerRespon(serverResponse);

    }

    @Test
    public void testGetUserShipping() throws IOException {
        login();
        ServerResponse serverResponse = shippingController.getUsersShipping(session);
        printServerRespon(serverResponse);
    }

    @Test
    public void testdeleteShipping() throws IOException {
        login();
        shippingController.deleteShipping(1l,session);
        ServerResponse serverResponse = shippingController.getUsersShipping(session);
        printServerRespon(serverResponse);
    }

}
