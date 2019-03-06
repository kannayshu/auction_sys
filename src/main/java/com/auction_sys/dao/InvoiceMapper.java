package com.auction_sys.dao;

import com.auction_sys.pojo.Invoice;
import org.apache.ibatis.annotations.Param;

public interface InvoiceMapper {
    int deleteByPrimaryKey(Long invoiceId);

    int insert(Invoice record);

    int insertSelective(Invoice record);

    Invoice selectByPrimaryKey(Long invoiceId);

    int updateByPrimaryKeySelective(Invoice record);

    int updateByPrimaryKey(Invoice record);

    int updateByOrderIdSelective(Invoice invoice);
}