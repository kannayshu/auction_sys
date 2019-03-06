package com.auction_sys.service.impl.utilimpl;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.common.constant.UserConst;
import com.auction_sys.service.util.StatusCheck;
import org.springframework.stereotype.Component;

/**
 * Created By liuda on 2018/5/26
 */
@Component("statusCheck")
public class StatusCheckImpl<T> implements StatusCheck<T> {

    public boolean checkActive(byte status){
        if(status== UserConst.UserStatusConst.UNACTIVE)
            return false;
        return true;
    }

    public boolean checkLimit(byte status){
        // if((status&(byte)2)==2)
        if(status==UserConst.UserStatusConst.LOGIN_LIMITED)
        return false;
        return true;
        }
@Override
public ServerResponse<T> check(byte status) {
        boolean bool = checkLimit(status);
        if (bool = false)
        return ServerResponse.createByError(UserConst.UserResponStateConst.LOGIN_LIMITED);
        bool = checkActive(status);
        if (bool = false)
        return ServerResponse.createByError(UserConst.UserResponStateConst.NOT_ACTIVE);
        return ServerResponse.createBySuccess();
        }
        }
