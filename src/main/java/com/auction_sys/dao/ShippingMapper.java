package com.auction_sys.dao;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.pojo.Shipping;
import com.auction_sys.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(@Param("shippingId") Long shippingId,@Param("userId") Long userId);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Long shippingId);

    List<Shipping> selectByUserId(Long userId);

    int countByUserId(Long userId);
    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);
}