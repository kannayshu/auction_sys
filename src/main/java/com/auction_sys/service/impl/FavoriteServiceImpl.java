package com.auction_sys.service.impl;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.common.constant.CommonConst;
import com.auction_sys.common.constant.FavoriteConst;
import com.auction_sys.dao.FavoriteMapper;
import com.auction_sys.dao.ProductMapper;
import com.auction_sys.pojo.Favorite;
import com.auction_sys.pojo.Product;
import com.auction_sys.service.FavoriteService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By liuda on 2018/7/23
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    FavoriteMapper favoriteMapper;

    @Autowired
    ProductMapper productMapper;

    public ServerResponse selectFavoriteProductList(int pageNum, int pageSize,Long userId){

        PageHelper.startPage(pageNum,pageSize);
        List<Long> productIdList = favoriteMapper.selectProductIdByUserId(userId);
        PageInfo pageInfo = new PageInfo(productIdList);
        List<Product> productList = new ArrayList<>(pageSize);
        productIdList.forEach((id)->productList.add(productMapper.selectSimpleResultByPrimaryKey(id)));
        pageInfo.setList(productList);
        if(productList!=null)
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS,pageInfo);
        return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
    }


    public ServerResponse addFavoriteProduct(Long userId,Long productId){
        int count = favoriteMapper.countByProductIdUserId(userId,productId);
        if (count>0)
            return ServerResponse.createByError(FavoriteConst.FavoriteStateConst.FAVORITE_REPITITION);
        Favorite favorite = new Favorite().setFavProductId(productId).setUserId(userId);
        int num = favoriteMapper.insert(favorite);
        if(num<=0)
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
        return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS);

    }

    public ServerResponse removeFavoriteProduct(Long userId,Long productId){
        Favorite favorite = new Favorite().setFavProductId(productId).setUserId(userId);
        int num = favoriteMapper.deleteByUserIdAndProductId(userId,productId);
        if(num<=0)
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
        return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS);

    }
}
