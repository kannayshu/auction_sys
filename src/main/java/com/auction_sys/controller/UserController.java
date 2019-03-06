package com.auction_sys.controller;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.common.constant.CommonConst;
import com.auction_sys.pojo.User;
import com.auction_sys.service.CommonService;
import com.auction_sys.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created By liuda on 2018/5/24
 */

@CrossOrigin("*")
@Controller()
@ResponseBody
@RequestMapping(value = "/user/")
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    CommonController commonController;
    @Autowired
    UserService userService;
    @GetMapping(value = "check_username")
    public ServerResponse<User> usernameIsRepitition(@RequestParam(value="username",required = true)String username){
        return userService.usernameIsRepitition(username);
    }
    @GetMapping(value = "check_email")
    public ServerResponse<User> emailIsRepitition(@RequestParam(value="email",required = true)String email){
        return userService.emailIsRepitition(email);
    }

    @PostMapping(value = "register")
    public ServerResponse<User> register(User user,@RequestParam(value="captcha",required = true) String captcha,HttpSession httpSession){
        System.out.println("captcha");
        ServerResponse serverResponse = commonController.verifyCaptcha(httpSession,captcha);
        if (!serverResponse.isSuccess())
            return serverResponse;

        user.setUserId(null);
        user.setStatus((byte)2);
        user.setRole((byte)1);
        return userService.register(user);

    }
    @PostMapping(value = "login")
    public ServerResponse<User> login(User user,@RequestParam(value="captcha",required = true) String captcha,HttpSession httpSession){
        ServerResponse serverResponse = commonController.verifyCaptcha(httpSession,captcha);
        logger.info(user.toString());
        if (!serverResponse.isSuccess())
            return serverResponse;
        serverResponse = userService.login(user);
        if (serverResponse.isSuccess())
            httpSession.setAttribute("user",serverResponse.getData());;
        return serverResponse;
    }

    //todo 方便测试使用接口，发布删
    @PostMapping(value = "login_without_captcha")
    public ServerResponse<User> loginWithoutChaptcha(User user,HttpSession httpSession){
        ServerResponse serverResponse = ServerResponse.createBySuccess();
        if (!serverResponse.isSuccess())
            return serverResponse;
        serverResponse = userService.login(user);
        if (serverResponse.isSuccess())
            httpSession.setAttribute("user",serverResponse.getData());;
        return serverResponse;
    }

    @GetMapping(value = "forget_password")
    public ServerResponse<User> forgetPassword(@RequestParam(value="email",required = true) String email){
        return userService.forgetPassword(email);
    }
    @PostMapping(value = "retrieve_password")
    public ServerResponse retrievePassword(@RequestParam(value="email",required = true) String email
            ,@RequestParam(value="password",required = true) String password
            ,@RequestParam("token") String token){
        return userService.retrievePassword(email, password, token);
    }


    @GetMapping(value = "get_user_info")
    public ServerResponse<User> getUserInfo(HttpSession httpSession){
        User user = (User) httpSession.getAttribute("user");
        if (user==null)
            return ServerResponse.createByError();
        return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS,user);
    }
}
