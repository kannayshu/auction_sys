package com.auction_sys.bo;

import com.auction_sys.pojo.ProductOrder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created By liuda on 2018/7/9
 */
public class ProductOrderBO {
    private ProductOrder productOrder;
    private ProductOrderBO(ProductOrderBO.ProductOrderBuilder builder){
       this.productOrder = new ProductOrder(builder);
    }

    public static class ProductOrderBuilder {
        private Long orderId;

        private Long depositId;

        private Long productId;

        private Long bidderId;

        private BigDecimal payment;

        private Byte paymentType;

        private Byte status;

        private Byte isDeleted;

        private Date gmtPayment;

        private Date gmtDeadline;

        private Date gmtEnd;

        private Date gmtClose;

        private Date gmtCreate;

        private Date gmtModified;

        public Long getDepositId() {
            return depositId;
        }

        public ProductOrderBuilder setDepositId(Long depositId) {
            this.depositId = depositId;
            return this;
        }

        public Date getGmtClose() {
            return gmtClose;
        }

        public ProductOrderBuilder setGmtClose(Date gmtClose) {
            this.gmtClose = gmtClose;
            return this;
        }

        public Long getOrderId() {
            return orderId;
        }

        public ProductOrderBuilder setOrderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Long getProductId() {
            return productId;
        }

        public ProductOrderBuilder setProductId(Long productId) {
            this.productId = productId;
            return this;
        }

        public Long getBidderId() {
            return bidderId;
        }

        public ProductOrderBuilder setBidderId(Long userId) {
            this.bidderId = userId;
            return this;
        }

        public BigDecimal getPayment() {
            return payment;
        }

        public ProductOrderBuilder setPayment(BigDecimal payment) {
            this.payment = payment;
            return this;
        }

        public Byte getPaymentType() {
            return paymentType;
        }

        public ProductOrderBuilder setPaymentType(Byte paymentType) {
            this.paymentType = paymentType;
            return this;
        }

        public Byte getStatus() {
            return status;
        }

        public ProductOrderBuilder setStatus(Byte status) {
            this.status = status;
            return this;
        }

        public Byte getIsDeleted() {
            return isDeleted;
        }

        public ProductOrderBuilder setIsDeleted(Byte isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public Date getGmtPayment() {
            return gmtPayment;
        }

        public ProductOrderBuilder setGmtPayment(Date gmtPayment) {
            this.gmtPayment = gmtPayment;
            return this;
        }

        public Date getGmtDeadline() {
            return gmtDeadline;
        }

        public ProductOrderBuilder setGmtDeadline(Date gmtDeadline) {
            this.gmtDeadline = gmtDeadline;
            return this;
        }

        public Date getGmtEnd() {
            return gmtEnd;
        }

        public ProductOrderBuilder setGmtEnd(Date gmtEnd) {
            this.gmtEnd = gmtEnd;
            return this;
        }

        public Date getGmtCreate() {
            return gmtCreate;
        }

        public ProductOrderBuilder setGmtCreate(Date gmtCreate) {
            this.gmtCreate = gmtCreate;
            return this;
        }

        public Date getGmtModified() {
            return gmtModified;
        }

        public ProductOrderBuilder setGmtModified(Date gmtModified) {
            this.gmtModified = gmtModified;
            return this;
        }
        public ProductOrderBO build(){
            return new ProductOrderBO(this);
        }
    }

    public ProductOrder  getProductOrder(){
        return this.productOrder;
    }
}
