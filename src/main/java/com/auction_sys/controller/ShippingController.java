package com.auction_sys.controller;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.pojo.Shipping;
import com.auction_sys.pojo.User;
import com.auction_sys.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.SecureRandom;

/**
 * Created By liuda on 2018/7/18
 */
@CrossOrigin("*")
@Component
@Controller
@RequestMapping("/shipping/")
@ResponseBody
public class ShippingController {

    @Autowired
    ShippingService shippingService;
    @PostMapping("add_shipping")
    public ServerResponse addShipping(Shipping shipping, HttpSession session){
        User user = (User) session.getAttribute("user");

        return shippingService.addShipping(shipping,user);
    }

    @PostMapping("update_shipping")
    public ServerResponse updateShipping(Shipping shipping, HttpSession session){
        User user = (User) session.getAttribute("user");

        return shippingService.updateShipping(shipping,user);
    }

    @PostMapping("delete_shipping")
    public ServerResponse deleteShipping(@RequestParam(value = "shipping_id",required = true)Long shippingId, HttpSession session){
        User user = (User) session.getAttribute("user");

        return shippingService.deleteShipping(shippingId,user);
    }
    @GetMapping("get_users_shipping")
    public ServerResponse getUsersShipping( HttpSession session){
        User user = (User) session.getAttribute("user");

        return shippingService.getUsersShipping(user);
    }

    @GetMapping("get_shipping")
    public ServerResponse getShipping( HttpSession session,@RequestParam(value = "shipping_id",required = true)Long shippingId){
        User user = (User) session.getAttribute("user");

        return shippingService.getShipping(user,shippingId);
    }
}
