package com.auction_sys.pojo;

import com.auction_sys.bo.ProductOrderBO;

import java.math.BigDecimal;
import java.util.Date;

public class ProductOrder{
    protected Long orderId;
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
    public ProductOrder(Long orderId, Long depositId, Long productId, Long userId, BigDecimal payment, Byte paymentType, Byte status,Date gmtPayment, Date gmtDeadline, Date gmtEnd, Date gmtClose, Date gmtCreate, Date gmtModified) {
        this.orderId = orderId;
        this.depositId = depositId;
        this.productId = productId;
        this.bidderId = userId;
        this.payment = payment;
        this.paymentType = paymentType;
        this.status = status;
        this.gmtPayment = gmtPayment;
        this.gmtDeadline = gmtDeadline;
        this.gmtEnd = gmtEnd;
        this.gmtClose = gmtClose;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public ProductOrder(ProductOrderBO.ProductOrderBuilder builder) {
        this.orderId = builder.getOrderId();
        this.depositId =  builder.getDepositId();
        this.productId =  builder.getProductId();
        this.bidderId =  builder.getBidderId();
        this.payment =  builder.getPayment();
        this.paymentType = builder.getPaymentType();
        this.status =  builder.getStatus();
        this.gmtPayment =  builder.getGmtPayment();
        this.gmtDeadline =  builder.getGmtDeadline();
        this.gmtEnd =  builder.getGmtEnd();
        this.gmtClose =  builder.getGmtClose();
        this.gmtCreate =  builder.getGmtCreate();
        this.gmtModified =  builder.getGmtModified();
        this.isDeleted = builder.getIsDeleted();
    }
    public ProductOrder() {
        super();
    }

    public Long getOrderId() {
        return orderId;
    }

    public ProductOrder setOrderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public Long getDepositId() {
        return depositId;
    }

    public ProductOrder setDepositId(Long depositId) {
        this.depositId = depositId;
        return this;
    }

    public Long getProductId() {
        return productId;
    }

    public ProductOrder setProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    public Long getBidderId() {
        return bidderId;
    }

    public ProductOrder setBidderId(Long bidderId) {
        this.bidderId = bidderId;
        return this;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public ProductOrder setPayment(BigDecimal payment) {
        this.payment = payment;
        return this;
    }

    public Byte getPaymentType() {
        return paymentType;
    }

    public ProductOrder setPaymentType(Byte paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public Date getGmtPayment() {
        return gmtPayment;
    }

    public ProductOrder setGmtPayment(Date gmtPayment) {
        this.gmtPayment = gmtPayment;
        return this;
    }

    public Byte getStatus() {
        return status;
    }

    public ProductOrder setStatus(Byte status) {
        this.status = status;
        return this;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public ProductOrder setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
        return this;
    }

    public Date getGmtClose() {
        return gmtClose;
    }

    public ProductOrder setGmtClose(Date gmtClose) {
        this.gmtClose = gmtClose;
        return this;
    }

    public Date getGmtEnd() {
        return gmtEnd;
    }

    public ProductOrder setGmtEnd(Date gmtEnd) {
        this.gmtEnd = gmtEnd;
        return this;
    }

    public Date getGmtDeadline() {
        return gmtDeadline;
    }

    public ProductOrder setGmtDeadline(Date gmtDeadline) {
        this.gmtDeadline = gmtDeadline;
        return this;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public ProductOrder setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
        return this;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public ProductOrder setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
        return this;
    }
}