package com.auction_sys.common.constant;

/**
 * Created By liuda on 2018/7/11
 */
public class CommonConst {

    public interface CommonResponStateConst {
        int OPRATION_SUCCESS = 10000;
        //验证码正确
        int CAPTCHA_RIGHT = 20010;
        //验证码错误
        int CAPTCHA_FAULT=20011;
        //非法操作
        int ILLEGAL_OPERATION = 11100;
        //参数错误
        int ILLEGAL_PARAMETETS = 11110;
        //发生未知错误
        int UNKONW_ERROR = 11111;



    }

    /**
     * Created by liuda
     */
    public static interface ResponseCode {

        int SUCCESS = 1;
        int ERROR = 0;
        int ILLEGAL_ARGUMENT = 2;
        int  NEED_LOGIN = 10;
    }
}
