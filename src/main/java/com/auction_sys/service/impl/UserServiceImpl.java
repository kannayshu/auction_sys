package com.auction_sys.service.impl;

import com.auction_sys.common.*;
import com.auction_sys.common.constant.CommonConst;
import com.auction_sys.common.constant.UserConst;
import com.auction_sys.dao.UserMapper;
import com.auction_sys.pojo.User;
import com.auction_sys.service.UserService;
import com.auction_sys.service.util.StatusCheck;
import com.auction_sys.util.*;
import com.auction_sys.vo.EmailNotity;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.UUID;


/**
 * Created By liuda on 2018/5/24
 */
@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    final static String HOST = PropertiesUtil.getProperty("item.host","true");
    Jedis jedis = RedisPool.getJedis();
    @Autowired
    StatusCheck statusCheck;
    @Autowired
    UserMapper userMapper ;
    @Override
    public ServerResponse usernameIsRepitition(String username) {
        int count = userMapper.checkUsername(username);

        return count==1?ServerResponse.createByError(UserConst.UserResponStateConst.USERNAME_REPITITION):
                ServerResponse.createBySuccess(UserConst.UserResponStateConst.USERNAME_UNREPITITION);
    }

    @Override
    public ServerResponse emailIsRepitition(String email) {
        int count = userMapper.checkEmail(email);

        return count==1?ServerResponse.createByError(UserConst.UserResponStateConst.EMAIL_REPITITION):
                ServerResponse.createBySuccess(UserConst.UserResponStateConst.EMAIL_UNREPITITION);
    }

    @Override

    public ServerResponse<User> register(User user) {
        ServerResponse serverResponse ;
        if(!user.getUsername().matches("[\\u4E00-\\u9FA5\\w]+$")
                ||user.getUsername()==null||user.getUsername().equals(""))
            return ServerResponse.createByError(UserConst.UserResponStateConst.USERNAME_UNPRIMITIVE);
        if (user.getEmail()==null||!user.getEmail().matches("[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]+$"))
            return ServerResponse.createByError(UserConst.UserResponStateConst.EMAIL_UNPRIMITIVE);
        if (!user.getPassword().matches("[\\w]{6,16}$")||user.getPassword()==null||user.getPassword().equals(""))
            return ServerResponse.createByError(UserConst.UserResponStateConst.PASSWORD_UNPRIMITIVE);
            serverResponse = this.usernameIsRepitition(user.getUsername());
        if (serverResponse.getStatus()!= CommonConst.ResponseCode.SUCCESS)
            return serverResponse;
        serverResponse  = this.emailIsRepitition(user.getEmail());
        if (serverResponse.getStatus()!= CommonConst.ResponseCode.SUCCESS)
            return serverResponse;
        try {
            user.setPassword(EncryptUtil.encrypt(user.getPassword()));
        } catch (Exception e) {
            logger.debug(e.getMessage(),e);
        }
        user.setStatus(UserConst.UserStatusConst.UNACTIVE);
        long count = userMapper.insertSelective(user);
        if(count>0){
            User u = new User();
            u.setUserId(user.getUserId());
            u.setUsername(user.getUsername());
            u.setEmail(user.getEmail());
            u.setStatus(UserConst.UserStatusConst.UNACTIVE);
            sentActiveEmail(u);
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS,u);
        }
        return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
    }


    @Override
    public ServerResponse<User> login(User user) {
        User u;
        if(user.getUsername()!=null){
            u = userMapper.selectByUsername(user.getUsername());
        }else if(user.getEmail()!=null){
            u = userMapper.selectByEmail(user.getEmail());
        }else
            return ServerResponse.createByError(UserConst.UserResponStateConst.ACCOUNT_INEXITENCE);
        String password =null;
        try {
            password = EncryptUtil.decrypt(u.getPassword());
            if(user.getPassword().equals(password)){
                if(!statusCheck.checkLimit(u.getStatus()))
                    return ServerResponse.createByError(UserConst.UserResponStateConst.LOGIN_LIMITED);
                user = new User(u.getUserId(),u.getRole(),u.getUsername(),null,u.getEmail(),u.getStatus());
                return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS,user);
            }else
                return ServerResponse.createByError(UserConst.UserResponStateConst.PASSWORD_ERROR);
        } catch (Exception e) {
            logger.debug(e.getMessage(),e);
        }

        return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
    }

    @Override
    public ServerResponse<User> sentActiveEmail(User user) {
        if (statusCheck.checkActive(user.getStatus()))
            return ServerResponse.createByError(UserConst.UserResponStateConst.HAD_ACTIVED);
        String activeCode = RomdomUtil.getRandomString(8);
        String url = HOST +"user/active_email?active_code="+activeCode+"&user_id="+user.getUserId();
        String  content = UserConst.EmailContent.ACTIVE_CONTENT.replace("${username}",user.getUsername())
                .replace("${URL}",url);
        NotifyThreadPool.pool.execute(new EmailNotity(content,user.getEmail()));
        jedis.set("activecode_"+user.getUserId(),activeCode);
        jedis.expire("activecode_"+user.getUserId(),60*10);
        return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS);
    }
    @Override
    public ServerResponse<User> activeEmail(long userId,String activeCode) {
        String activeCode0 = jedis.get(("activecode_"+userId));
        if(activeCode==null)
            return ServerResponse.createByError(UserConst.UserResponStateConst.ACTIVE_FAIL);
        if (activeCode.equals(activeCode0)){
            jedis.del("activecode_"+userId);
            User u = new User();
            u.setUserId(userId);
            u.setStatus((byte)1);
            userMapper.updateByPrimaryKeySelective(u);
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS);
        }
        return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
    }

    public ServerResponse retrievePassword(String email,String password,String updateToken){
        String token = jedis.get("forget_email_account_"+email);
        if(token==null)
            return ServerResponse.createByError(UserConst.UserResponStateConst.UPDATE_TOKEN_INEXITENCE);
        if (token.equals(updateToken)){
            jedis.del("forget_email_account_"+email);
            int count = userMapper.updatePasswordByEmail(email,EncryptUtil.encrypt(password));
            if (count>0)
                return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS);
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
        }
        return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
    }

    public ServerResponse forgetPassword(String email){
        int count = userMapper.checkEmail(email);
        if(count!=1)
            return ServerResponse.createByError(UserConst.UserResponStateConst.EMAIL_INEXITENCE);
        String updateToken = UUID.randomUUID().toString();
        jedis.set("forget_email_account_"+email,updateToken);
        jedis.expire("forget_email_account_"+email,60*10);
        //todo
        String url = HOST +"user/forget_email?token="+updateToken+"&email="+email;
        String  content = UserConst.EmailContent.FORGET_PASSWORD.replace("${URL}",url);
        boolean bool = MailUtil.sendMail(email,content);
        if(bool)
            return ServerResponse.createBySuccess(UserConst.UserResponStateConst.EMAIL_HAD_SEND);
        return ServerResponse.createByError(UserConst.UserResponStateConst.EMAIL_HAD_NOT_SEND);
    }

    public ServerResponse updatePassword(String initPassword,String updatePassword,User user) {
        User u = userMapper.selectByPrimaryKey(user.getUserId());
        if(u==null)
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
        try {
            if (!EncryptUtil.decrypt(u.getPassword()).equals(initPassword))
                return ServerResponse.createByError(UserConst.UserResponStateConst.INIT_PASSWORD_ERROR);
        } catch (Exception e) {

            e.printStackTrace();
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
        }
        int num = userMapper.updatePasswordByUserId(user.getUserId(),EncryptUtil.encrypt(updatePassword));
        if (num>0){
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS);
        }


        return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
    }

}