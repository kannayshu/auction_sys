package com.auction_sys.controller;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.pojo.User;
import com.auction_sys.service.ProductService;
import com.auction_sys.pojo.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created By liuda on 2018/6/2
 */
@CrossOrigin("*")
@Controller
@RequestMapping("/product/")
@ResponseBody
public class ProductController {

    private final static Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    ProductService productService;

    @PostMapping("add_product")
    public ServerResponse addProduct(Product product, HttpSession httpSession){
        User u = (User) httpSession.getAttribute("user");
        logger.info("info"+product.toString());
        return productService.addProduct(product,u);
    }

    @GetMapping("get_products")
    public ServerResponse getProductList(@RequestParam(value="page_num" ,defaultValue = "1") int pageNum,
                                         @RequestParam(value="page_size",defaultValue = "10")int pageSize,
                                         @RequestParam(value="category_id",defaultValue = "-1")int categoryId){
        if (categoryId==-1)
            return productService.selectProductList(pageNum,pageSize);
        return productService.selectProductListByCategory(pageNum,pageSize,categoryId);
    }

    @GetMapping("get_details")
    public ServerResponse getProductDetail(@RequestParam("product_id") Long productId){
        return productService.selectProduct(productId);
    }

    @GetMapping("get_sample")
    public ServerResponse getProductSample(@RequestParam("product_id") Long productId){
        return productService.selectProductSample(productId);
    }

    @PostMapping("update_product")
    public ServerResponse updateProduct(Product product, HttpSession httpSession){
        logger.debug(product.toString());
        User u = (User) httpSession.getAttribute("user");
        return productService.updateProduct(product,u);
    }

    @PostMapping("putaway")
    public ServerResponse switchStatus( HttpSession httpSession,@RequestParam("product_id")Long productId){
        User u = (User) httpSession.getAttribute("user");
        return productService.putawayProduct(u,productId);
    }

    @GetMapping("search_product")
    public ServerResponse searchProduct(@RequestParam(value="page_num" ,defaultValue = "1") int pageNum,
                                        @RequestParam(value="page_size",defaultValue = "10")int pageSize,
                                        @RequestParam(value="content",required = true)String content,
                                        @RequestParam(value = "category_id",required = false,defaultValue = "-1")Integer categoryId,
                                        HttpSession httpSession) throws UnsupportedEncodingException {

        return productService.searchProductSelective(content,categoryId,pageNum,pageSize);
    }

    @GetMapping("get_users_product")
    public ServerResponse getProductByUser(@RequestParam(value="page_num" ,defaultValue = "1") int pageNum,
                                         @RequestParam(value="page_size",defaultValue = "10")int pageSize,
                                         @RequestParam(value="user_id",defaultValue = "-1")Long userId,HttpSession session){
        User u = (User) session.getAttribute("user");
        if(userId==-1)
            userId = u.getUserId();
        return productService.listResultBySellerId(pageNum,pageSize,userId);
    }




}
