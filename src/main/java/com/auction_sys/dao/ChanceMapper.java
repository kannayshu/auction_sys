package com.auction_sys.dao;

import com.auction_sys.pojo.Chance;

public interface ChanceMapper {
    int deleteByPrimaryKey(Long chanceId);

    int insert(Chance record);

    int insertSelective(Chance record);

    Chance selectByPrimaryKey(Long chanceId);

    int updateByPrimaryKeySelective(Chance record);

    int updateByPrimaryKey(Chance record);
}