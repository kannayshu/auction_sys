package com.auction_sys.service;

import com.auction_sys.common.ServerResponse;

/**
 * Created By liuda on 2018/7/23
 */
public interface FavoriteService {

    public ServerResponse selectFavoriteProductList(int pageNum, int pageSize, Long userId);
    public ServerResponse addFavoriteProduct(Long userId,Long productId);
    public ServerResponse removeFavoriteProduct(Long userId,Long productId);
}
