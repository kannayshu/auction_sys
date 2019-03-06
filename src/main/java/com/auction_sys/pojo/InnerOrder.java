package com.auction_sys.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class InnerOrder {
    private Long innerOrderId;

    private Long productOrderId;

    private BigDecimal payment;

    private String remark;

    private Byte isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    public InnerOrder() {
    }

    public InnerOrder(Long innerOrderId, Long productOrderId, BigDecimal payment, String remark, Byte isDeleted, Date gmtCreate, Date gmtModified) {
        this.innerOrderId = innerOrderId;
        this.productOrderId = productOrderId;
        this.payment = payment;
        this.remark = remark;
        this.isDeleted = isDeleted;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public Long getInnerOrderId() {
        return innerOrderId;
    }

    public InnerOrder setInnerOrderId(Long innerOrderId) {
        this.innerOrderId = innerOrderId;
        return this;
    }

    public Long getProductOrderId() {
        return productOrderId;
    }

    public InnerOrder setProductOrderId(Long productOrderId) {
        this.productOrderId = productOrderId;
        return this;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public InnerOrder setPayment(BigDecimal payment) {
        this.payment = payment;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public InnerOrder setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public InnerOrder setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
        return this;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public InnerOrder setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
        return this;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public InnerOrder setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
        return this;
    }
}