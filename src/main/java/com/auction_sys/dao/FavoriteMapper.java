package com.auction_sys.dao;

import com.auction_sys.pojo.Favorite;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FavoriteMapper {
    int deleteByPrimaryKey(Long favId);
    int deleteByUserIdAndProductId(@Param("userId")Long userId,@Param("productId")Long productId);
    int insert(Favorite record);

    int insertSelective(Favorite record);

    Favorite selectByPrimaryKey(Long favId);

    List<Long> selectProductIdByUserId(Long userId);
    int countByProductIdUserId(@Param("userId")Long userId,@Param("productId") Long productId);
    int updateByPrimaryKeySelective(Favorite record);

    int updateByPrimaryKey(Favorite record);
}