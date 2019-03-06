package com.auction_sys.controller;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.common.constant.CommonConst;
import com.auction_sys.pojo.User;
import com.auction_sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created By liuda on 2018/5/27
 */
@CrossOrigin("*")
@Controller
@ResponseBody
@RequestMapping(value = "/user/")
public class LoginedUserController {

    @Autowired
    UserService userService;
    @GetMapping("send_active_email")
    public ServerResponse activeEmail(HttpSession httpSession){
        User u = (User) httpSession.getAttribute("user");
        return userService.sentActiveEmail(u);
    }

    @GetMapping("active_email")
    public ServerResponse activeEmail(@RequestParam("active_code") String activecCode, HttpSession httpSession, @RequestParam("user_id")Long  userId){
         return userService.activeEmail(userId,activecCode);
    }

    @GetMapping("logout")
    public ServerResponse logout(HttpSession httpSession){

        httpSession.setAttribute("user",null);
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS);
    }
    @PostMapping("update_password")
    public ServerResponse updatePassword(@RequestParam(value = "init_password",required = true)String initPassword,
                                         @RequestParam(value="update_password",required = true)String updatePassword,HttpSession httpSession){
        User u = (User) httpSession.getAttribute("user");
        return userService.updatePassword(initPassword,updatePassword,u);

    }
}
