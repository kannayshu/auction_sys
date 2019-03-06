package com.auction_sys.dao;

import com.auction_sys.common.constant.OrderConst;
import com.auction_sys.pojo.ProductOrder;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public interface ProductOrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(ProductOrder record);

    int insertSelective(ProductOrder record);

    ProductOrder selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(ProductOrder record);

    int updateByPrimaryKey(ProductOrder record);

    ProductOrder selectByBidderIdAndOrderId(@Param("bidderId") long bidderId, @Param("orderId")long orderId);

    int updateByDepositIdSelective(ProductOrder productOrder);

    long selectOrderIdByProductIdAndStatus(@Param("productId")Long productId, @Param("status")Byte status);
    ProductOrder selectOrderByProductIdAndUserId(@Param("productId")Long productId, @Param("userId")Long userId);

    BigDecimal selectPaymentByOrderId(@Param("orderId")Long orderId);

    ProductOrder selectOrderByProductIdAndStatus(@Param("productId")Long productId, @Param("status")Byte status);

    int updateStatusByOrderIdAndStatus(@Param("orderId")Long orderId, @Param("status1")Byte status1,@Param("status2")Byte status2);

    int updateStatusAndDeadlineByOrderIdAndStatus(@Param("orderId")Long orderId, @Param("status1")Byte status1
            , @Param("status2")Byte status2, @Param("deadline")Date date);

    int updateStatusByDepositIdAndStatus(@Param("status1")Byte status1, @Param("status2")Byte status2
            ,@Param("depositId")long deposiId );

    List<Long> selectOrderIdByGmtDeadlineAndStatus(@Param("status")Byte status);

    Long selectDepositIdByPrimaryKey(@Param("orderId") Long orderId);
    Long selectProductIdByPrimaryKey(@Param("orderId") Long orderId);
    List<Long> selectOrderIdByStatus(@Param("status")Byte status);
    int deleteByProductId(Long productId);

    BigDecimal selectPaymentByPrimaryKey(Long orderId);
    Byte getStatusByPrimaryKeyAndUserId(@Param("orderId")long orderId,@Param("bidderId") long bidderId);
}