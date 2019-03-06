package com.auction_sys.pojo;

import com.auction_sys.common.vaild.NotNull;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created By liuda on 2018/7/16
 */
public class Order {

    protected Long depositId;

    protected Long productId;

    protected Long bidderId;

    protected BigDecimal payment;

    protected Byte paymentType;

    protected Date gmtPayment;

    protected Byte status;

    protected Byte isDeleted;

    protected Date gmtClose;

    protected Date gmtEnd;

    protected Date gmtDeadline;

    protected Date gmtCreate;

    protected Date gmtModified;
}
