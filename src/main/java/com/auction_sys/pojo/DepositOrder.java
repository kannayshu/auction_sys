package com.auction_sys.pojo;

import com.auction_sys.bo.DepositOrderBO;

import java.math.BigDecimal;
import java.util.Date;

public class DepositOrder {

    protected Long depositId;

    protected Long productId;

    protected Long payerId;

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

    protected Byte type;

    public DepositOrder(Long depositId, Byte type ,Long productId, Long payerId, BigDecimal payment, Byte paymentType, Date gmtPayment, Byte status, Date gmtClose, Date gmtEnd, Date gmtDeadline,Date gmtCreate, Date gmtModified) {
        this.depositId = depositId;
        this.type = type;
        this.productId = productId;
        this.payerId = payerId;
        this.payment = payment;
        this.paymentType = paymentType;
        this.gmtPayment = gmtPayment;
        this.status = status;
        this.gmtClose = gmtClose;
        this.gmtEnd = gmtEnd;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
        this.gmtDeadline = gmtDeadline;
    }

    public DepositOrder(DepositOrderBO.DepositOrderBuilder builder) {
        this.depositId = builder.getDepositId();
        this.type = builder.getType();
        this.productId = builder.getProductId();
        this.payerId = builder.getPayerId();
        this.payment = builder.getPayment();
        this.paymentType = builder.getPaymentType();
        this.gmtPayment =builder.getGmtPayment();
        this.status = builder.getStatus();
        this.gmtClose = builder.getGmtClose();
        this.gmtEnd = builder.getGmtEnd();
        this.gmtCreate = builder.getGmtCreate();
        this.gmtModified = builder.getGmtModified();
        this.isDeleted = builder.getIsDeleted();
        this.gmtDeadline = builder.getGmtDeadline();
    }

    public DepositOrder() {
        super();
    }

    public Byte getType() {
        return type;
    }

    public DepositOrder setType(Byte type) {
        this.type = type;
        return this;
    }

    public Long getDepositId() {
        return depositId;
    }

    public void setDepositId(Long depositId) {
        this.depositId = depositId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getPayerId() {
        return payerId;
    }

    public void setPayerId(Long payerId) {
        this.payerId = payerId;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public Byte getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Byte paymentType) {
        this.paymentType = paymentType;
    }

    public Date getGmtPayment() {
        return gmtPayment;
    }

    public void setGmtPayment(Date gmtPayment) {
        this.gmtPayment = gmtPayment;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getGmtClose() {
        return gmtClose;
    }

    public void setGmtClose(Date gmtClose) {
        this.gmtClose = gmtClose;
    }

    public Date getGmtEnd() {
        return gmtEnd;
    }

    public void setGmtEnd(Date gmtEnd) {
        this.gmtEnd = gmtEnd;
    }

    public Date getGmtDeadline() {
        return gmtDeadline;
    }

    public void setGmtDeadline(Date gmtDeadline) {
        this.gmtDeadline = gmtDeadline;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}