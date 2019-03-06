package com.auction_sys.service;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.pojo.User;
import com.auction_sys.pojo.Product;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created By liuda on 2018/5/30
 */
public interface ProductService {
    ServerResponse addProduct(Product product, User user);
    ServerResponse updateProduct(Product product, User user);
    ServerResponse selectProduct(long pid);
    ServerResponse selectProductSample(long pid);
    ServerResponse selectProductList(int pageNum,int pageSize);
    ServerResponse selectProductListByCategory(int pageNum,int pageSize,int categoryId);
    ServerResponse searchProductSelective(String content,Integer categoryId,int pageNum,int pageSize);
    ServerResponse putawayProduct(User user,Long productId);
    public ServerResponse listResultBySellerId(int pageNum,int pageSize,Long sellerId);
}
