package com.auction_sys.vo;

import com.auction_sys.pojo.Product;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created By liuda on 2018/7/9
 */

public class OrderVO {

    private Long productOrderId;

    private Long depositOrderId;

    private Product product;

    private Long bidderId;

    private BigDecimal productPayment;

    private BigDecimal cashDeposit;

    private Byte status;

    private Date paymentTime;

    private Date deadline;

    private Date endTime;

    private Date closeTime;

    private Date gmtCreate;

    private Date gmtModified;

    public Long getProductOrderId() {
        return productOrderId;
    }

    public OrderVO setProductOrderId(Long productOrderId) {
        this.productOrderId = productOrderId;
        return this;
    }

    public Long getDepositOrderId() {
        return depositOrderId;
    }

    public OrderVO setDepositOrderId(Long depositOrderId) {
        this.depositOrderId = depositOrderId;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public OrderVO setProduct(Product product) {
        this.product = product;
        return this;
    }

    public Long getBidderId() {
        return bidderId;
    }

    public OrderVO setBidderId(Long bidderId) {
        this.bidderId = bidderId;
        return this;
    }

    public BigDecimal getProductPayment() {
        return productPayment;
    }

    public OrderVO setProductPayment(BigDecimal productPayment) {
        this.productPayment = productPayment;
        return this;
    }

    public BigDecimal getCashDeposit() {
        return cashDeposit;
    }

    public OrderVO setCashDeposit(BigDecimal cashDeposit) {
        this.cashDeposit = cashDeposit;
        return this;
    }

    public Byte getStatus() {
        return status;
    }

    public OrderVO setStatus(Byte status) {
        this.status = status;
        return this;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public OrderVO setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
        return this;
    }

    public Date getDeadline() {
        return deadline;
    }

    public OrderVO setDeadline(Date deadline) {
        this.deadline = deadline;
        return this;
    }

    public Date getEndTime() {
        return endTime;
    }

    public OrderVO setEndTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public OrderVO setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
        return this;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public OrderVO setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
        return this;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public OrderVO setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
        return this;
    }
}
