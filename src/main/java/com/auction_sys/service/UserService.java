package com.auction_sys.service;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.pojo.User;

import javax.servlet.http.HttpSession;

/**
 * Created By liuda on ${date}
 */
public interface UserService {
    //检查用户名是否重复
    ServerResponse<User> usernameIsRepitition(String username);
    ServerResponse<User> emailIsRepitition(String email);
    ServerResponse<User> register(User user);
    ServerResponse<User> login(User user);
    ServerResponse<User> activeEmail(long userId,String activeCode);
    ServerResponse<User> sentActiveEmail(User user);
    public ServerResponse forgetPassword(String email);
    public ServerResponse retrievePassword(String email,String password,String updateToken);
    public ServerResponse updatePassword(String initPassword,String updatePassword,User user);

}
