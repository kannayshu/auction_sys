package com.auction_sys.dao;

import com.auction_sys.pojo.InnerOrder;

public interface InnerOrderMapper {
    int deleteByPrimaryKey(Long innerOrderId);

    int insert(InnerOrder record);

    int insertSelective(InnerOrder record);

    InnerOrder selectByPrimaryKey(Long innerOrderId);

    int updateByPrimaryKeySelective(InnerOrder record);

    int updateByPrimaryKey(InnerOrder record);
}