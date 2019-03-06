package com.auction_sys.common.constant;

/**
 * Created By liuda on 2018/7/11
 */
public class UserConst {
    public interface Role{
        int ROLE_CUSTOMER = 1; //普通用户
        int ROLE_ADMIN = 2;//管理员
    }

    public interface EmailContent{
        String ACTIVE_CONTENT = "尊敬的${username}，您好！\r\n<a href='${URL}'>点击激活</a>，" +
                "本邮件十分钟内有效\r\n若不是您本人的操作，请忽略此邮件"; //邮箱激活内容
        String FORGET_PASSWORD = "您好！\r\n<a href='${URL}'>点击修改密码</a>，" +
                "本邮件十分钟内有效\r\n若不是您本人的操作，请忽略此邮件"; //邮箱内容

    }

    public interface UserResponStateConst {
        //用户名不重复
        int  USERNAME_UNREPITITION = 10010;
        //用户名重复
        int USERNAME_REPITITION = 10011;
        //用户名格式不符合规范
        int USERNAME_UNPRIMITIVE = 10012;
        //邮箱不重复
        int EMAIL_UNREPITITION = 10020;
        //邮箱重复
        int  EMAIL_REPITITION = 10021;
        //邮箱格式不符合规范
        int EMAIL_UNPRIMITIVE = 10022;
        //密码格式不符合规范
        int  PASSWORD_UNPRIMITIVE = 10031;
        //密码错误
        int  PASSWORD_ERROR = 10032;
        int INIT_PASSWORD_ERROR = 10034;
        //账号不存在
        int  ACCOUNT_INEXITENCE = 10033;
        /* status 字段信息常量*/
        //用户登录受限
        int   LOGIN_LIMITED = 10041;
        //用户未激活ED
        int    NOT_ACTIVE = 10042;
        //用户已被激活
        int   HAD_ACTIVED = 10043;
        //激活失败
        int     ACTIVE_FAIL = 10044;
        int EMAIL_HAD_SEND = 10050;
        int EMAIL_HAD_NOT_SEND = 10051;
        int EMAIL_INEXITENCE = 10052;
        int UPDATE_TOKEN_INEXITENCE = 10053;

    }

    public interface UserStatusConst{
        byte UNACTIVE = 2;
        byte ACTIVE = 1;
        byte LOGIN_LIMITED = 0;
    }
}
