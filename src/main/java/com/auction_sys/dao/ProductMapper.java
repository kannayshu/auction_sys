package com.auction_sys.dao;

import com.auction_sys.pojo.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(@Param("product") Product product);

    Product sellectSellerIdAndStatusByPrimaryKey(Long productId);
    List<Product> listResult();

    List<Product> listResultByCategory(int categoryId);

    List<Product> searchSelective(Product product);

    Product selectSimpleResultByPrimaryKey(Long id);

    List<Long> selectDeadlineIdByStatus(@Param("status")Byte status);
    int updateStatusByPrimaryKey(@Param("status")Byte status,@Param("productId")Long id);
    int updateStatusByPrimaryKeyAndUserId(@Param("productId")Long productId,@Param("userId")Long userId,
                                          @Param("status1")Byte status1,@Param("status2")Byte status2);

    List<Product> listResultBySellerId(long sellerId);

    Long selectSellerIdByPrimaryKey(long productId);


    int updateCurrentPriceByProductId(@Param("currentPrice")BigDecimal payment,@Param("productId")Long productId);

}