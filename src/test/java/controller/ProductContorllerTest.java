package controller;

import com.auction_sys.bo.ProductBO;
import com.auction_sys.common.ServerResponse;
import com.auction_sys.common.constant.ProductConst;
import com.auction_sys.controller.ProductController;
import com.auction_sys.controller.UserController;
import com.auction_sys.pojo.Product;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.TestBase;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created By liuda on 2018/7/15
 */
public class ProductContorllerTest extends TestBase{

    @Autowired
    ProductController productController;

    @Test
    public void addProduct() throws IOException {
        ProductBO.ProductBuilder builder = new ProductBO.ProductBuilder();
        Product product =  builder.setCashDeposit(new BigDecimal(10)).setCategoryId(100026).setDetail("detail_test")
                .setGmtDeadline(new Date()).setMainImage("main").setName("老白干test").setReservePrice(new BigDecimal(10))
                .setSchoolId(1).setStartingPrice(new BigDecimal(10)).setScale(new BigDecimal(10))
                .setSubTitle("title").setSubImage("subimage").setStatus(ProductConst.ProductStatusConst.DRAFT_STATUS).build().getProduct();
        login();

        ServerResponse serverResponse =productController.addProduct(product,session);
        printServerRespon(serverResponse);

    }

    @Test
    public void updateProduct1() throws IOException {
        ProductBO.ProductBuilder builder = new ProductBO.ProductBuilder();
        Product product =  builder.setId(37l).setCashDeposit(new BigDecimal(11)).setCategoryId(100026).setDetail("detail_test")
                .setGmtDeadline(new Date()).setMainImage("main").setName("老白干1").setReservePrice(new BigDecimal(10))
                .setSchoolId(1).setStartingPrice(new BigDecimal(10)).setScale(new BigDecimal(10))
                .setSubTitle("title").setSubImage("subimage").setStatus(ProductConst.ProductStatusConst.BIDDING).build().getProduct();
        login();

        ServerResponse serverResponse =productController.updateProduct(product,session);
        printServerRespon(serverResponse);

    }


    @Test
    public void searchProduct() throws IOException {
        ServerResponse serverResponse = productController.searchProduct(1,10,"智能",-1,session);
        printServerRespon(serverResponse);
    }

    @Test
    public void getUsersProduct() throws IOException {
        ServerResponse serverResponse = productController.getProductByUser(1,10,22l,session);
        printServerRespon(serverResponse);
    }

    @Test
    public void getProductDeatil() throws IOException {
        ServerResponse serverResponse = productController.getProductDetail(41l);
        printServerRespon(serverResponse);
    }


    @Test
    public void getProductList() throws IOException {
        ServerResponse serverResponse = productController.getProductList(1,10,100026);
        printServerRespon(serverResponse);
    }


}
