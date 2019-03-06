package com.auction_sys.controller;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.pojo.User;
import com.auction_sys.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created By liuda on 2018/7/24
 */
@Controller
@CrossOrigin("*")
@RequestMapping("/favorite/")
@ResponseBody
public class FavoriteController {

    @Autowired
    FavoriteService favoriteService;

        @GetMapping("get_users_favorite")
    public ServerResponse getUserFavorite(@RequestParam(value="page_num" ,defaultValue = "1") int pageNum,
                                          @RequestParam(value="page_size",defaultValue = "10")int pageSize,
                                          HttpSession session){

        User user = (User) session.getAttribute("user");
        return favoriteService.selectFavoriteProductList(pageNum,pageSize,user.getUserId());
    }

    @PostMapping("add_favorite_product")
    public ServerResponse addFavoriteProduct(@RequestParam(value="product_id" ,required =true) Long productId,

                                             HttpSession session){
        User user = (User) session.getAttribute("user");
        return favoriteService.addFavoriteProduct(user.getUserId(),productId);
    }

    @PostMapping("del_favorite_product")
    public ServerResponse removeFavoriteProduct(@RequestParam(value="product_id" ,required =true) Long productId,
                                                HttpSession session){
        User user = (User) session.getAttribute("user");
        return favoriteService.removeFavoriteProduct(user.getUserId(),productId);
    }
}
