package com.auction_sys.service.impl;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.common.constant.CommonConst;
import com.auction_sys.common.constant.ShippingConst;
import com.auction_sys.common.vaild.ParamVaild;
import com.auction_sys.dao.ShippingMapper;
import com.auction_sys.pojo.Shipping;
import com.auction_sys.pojo.User;
import com.auction_sys.service.CommonService;
import com.auction_sys.service.ShippingService;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created By liuda on 2018/7/18
 */
@Service
public class ShippingServiceImpl implements ShippingService{

    private final static Logger logger = LoggerFactory.getLogger(CommonService.class);
    @Autowired
    ShippingMapper shippingMapper;

    ParamVaild paramVaild = ParamVaild.getInstance();

    public ServerResponse addShipping(Shipping shipping, User user){
        int num = count(user.getUserId());
        if (num>= ShippingConst.SHIPPING_UPPER_LIMIT)
            return ServerResponse.createByError(ShippingConst.ShippingResponStateConst.SHIPPING_NUMBER_LIMITED);
        if (!paramVaild.insertable(shipping)){
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.ILLEGAL_PARAMETETS);
        }
        shipping.setUserId(user.getUserId());
        num = shippingMapper.insert(shipping);
        if (num>0)
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS);
        return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
    }

    public ServerResponse updateShipping(Shipping shipping, User user){
        if (!paramVaild.updateable(shipping)){
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.ILLEGAL_PARAMETETS);
        }
        shipping.setUserId(user.getUserId());
        int num = shippingMapper.updateByPrimaryKeySelective(shipping);
        if (num>0)
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS);
        return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
    }

    public ServerResponse deleteShipping(Long shippingId, User user){

        int num = shippingMapper.deleteByPrimaryKey(shippingId,user.getUserId());
        if (num>0)
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS);
        return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
    }

    public ServerResponse getUsersShipping(User user){
        List shippings = shippingMapper.selectByUserId(user.getUserId());
        Map maps =  new HashMap();
        maps.put("list",shippings);
        return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS,maps);
    }

    public ServerResponse getShipping(User user,Long id){
        Shipping shipping = shippingMapper.selectByPrimaryKey(id);
        if (!shipping.getUserId().equals(user.getUserId()))
            return ServerResponse.createByErrorIllegalParam();
        return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS,shipping);
    }


    public int count(Long userId){
        return shippingMapper.countByUserId(userId);
    }
}
