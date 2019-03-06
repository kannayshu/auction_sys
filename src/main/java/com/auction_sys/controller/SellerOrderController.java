package com.auction_sys.controller;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.pojo.User;
import com.auction_sys.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created By liuda on 2018/7/22
 */
@CrossOrigin("*")
@Controller
@RequestMapping(value = "/order/")
@ResponseBody
public class SellerOrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    OrderService orderService;

    @PostMapping("seller_deposit_pay")
    public ServerResponse depositPay(HttpSession session,@RequestParam(value = "deposit_id",required = true) Long depositId){
        User user = (User) session.getAttribute("user");
        return orderService.sellerDepositPay(depositId,user.getUserId());
    }

    @PostMapping("create_deposit_oder")
    public ServerResponse createDepositOrderForSeller(HttpSession session,@RequestParam(value = "product_id",required = true)Long productId){
        User user = (User) session.getAttribute("user");
        return orderService.createDepositOrderForSeller(productId,user.getUserId());
    }

    @PostMapping("deliver_goods")
    public ServerResponse deliverGoods(HttpSession session,@RequestParam(value = "product_id",required = true)Long productId
    ,@RequestParam("logistics_number")String logisticsNumber,@RequestParam("logistics_company")String logisticsCompany){
        User user = (User) session.getAttribute("user");
        return orderService.sellerDeliverGoods(productId,logisticsNumber,logisticsCompany,user.getUserId());
    }
}
