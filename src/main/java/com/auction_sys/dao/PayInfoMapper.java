package com.auction_sys.dao;

import com.auction_sys.pojo.PayInfo;

public interface PayInfoMapper {
    int deleteByPrimaryKey(Long payInfoId);

    int insert(PayInfo record);

    int insertSelective(PayInfo record);

    PayInfo selectByPrimaryKey(Long payInfoId);

    int updateByPrimaryKeySelective(PayInfo record);

    int updateByPrimaryKey(PayInfo record);

    int updateByOrderIdSelective(PayInfo record);

    String selectBuyerLogonIdByOrderId(Long orderId);
}