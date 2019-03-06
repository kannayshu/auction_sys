package com.auction_sys.dao;

import com.auction_sys.pojo.DepositOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepositOrderMapper {
    int deleteByPrimaryKey(Long depositId);
    int insert(DepositOrder record);

    int insertSelective(DepositOrder record);

    DepositOrder selectByPrimaryKey(Long depositId);

    int updateByPrimaryKeySelective(DepositOrder record);

    int updateByPrimaryKey(DepositOrder record);

    DepositOrder selectByUserIdAndDepositId(@Param("payerId") long payerId,@Param("depositId")long depositId);
    Byte getStatusByPrimaryKeyAndUserId(@Param("depositId")long depositId,@Param("payerId") long payerId);
    long selectDepositIdByProductIdAndStatusAndType(@Param("productId")Long productId, @Param("status")Byte status,@Param("type") Byte type );
    int updateStatusByDepositIdAndStatus(@Param("status1")Byte status1, @Param("status2")Byte status2
            ,@Param("depositId")long deposiId );
    int updateStatusByDepositId(@Param("depositId")Long depositId, @Param("status")Byte status);
    List<Long> selectDepositIdByGmtdeadlineAndStatus(@Param("status")Byte status);

    Long selectDepositIdByPayerIdAndTypeAndProductId(@Param("payerId") Long payerId,@Param("type") Byte type
            ,@Param("productId")Long productId);
    DepositOrder selectDepositByPayerIdAndTypeAndProductId(@Param("payerId") Long payerId,@Param("type") Byte type
            ,@Param("productId")Long productId);

    int deleteByProductIdAndType(@Param("productId") Long productId,@Param("type")Byte type);
}