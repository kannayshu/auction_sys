package com.auction_sys.bo;

import com.auction_sys.pojo.DepositOrder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created By liuda on 2018/7/9
 */
public class DepositOrderBO {

    private DepositOrder depositOrder;
    private DepositOrderBO(DepositOrderBuilder builder){
        this.depositOrder = new DepositOrder(builder);
    }
    public static class DepositOrderBuilder {
        private Long depositId;

        private Long productId;

        private Long payerId;

        private BigDecimal payment;

        private Byte paymentType;

        private Date gmtPayment;

        private Byte type;

        private Byte status;

        private Byte isDeleted;

        private Date gmtClose;

        private Date gmtEnd;

        private Date gmtDeadline;

        private Date gmtCreate;

        private Date gmtModified;

        public Long getPayerId() {
            return payerId;
        }

        public Byte getType() {
            return type;
        }

        public DepositOrderBuilder setType(Byte type) {
            this.type = type;
            return this;
        }

        public DepositOrderBuilder setPayerId(Long payerId) {
            this.payerId = payerId;
            return this;
        }

        public Long getDepositId() {
            return depositId;
        }

        public DepositOrderBuilder setDepositId(Long depositId) {
            this.depositId = depositId;
            return this;
        }

        public Long getProductId() {
            return productId;
        }

        public DepositOrderBuilder setProductId(Long productId) {
            this.productId = productId;
            return this;
        }

        public BigDecimal getPayment() {
            return payment;
        }

        public DepositOrderBuilder setPayment(BigDecimal payment) {
            this.payment = payment;
            return this;
        }

        public Byte getPaymentType() {
            return paymentType;
        }

        public DepositOrderBuilder setPaymentType(Byte paymentType) {
            this.paymentType = paymentType;
            return this;
        }

        public Date getGmtPayment() {
            return gmtPayment;
        }

        public DepositOrderBuilder setGmtPayment(Date gmtPayment) {
            this.gmtPayment = gmtPayment;
            return this;
        }

        public Byte getStatus() {
            return status;
        }

        public DepositOrderBuilder setStatus(Byte status) {
            this.status = status;
            return this;
        }

        public Byte getIsDeleted() {
            return isDeleted;
        }

        public DepositOrderBuilder setIsDeleted(Byte isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public Date getGmtClose() {
            return gmtClose;
        }

        public DepositOrderBuilder setGmtClose(Date gmtClose) {
            this.gmtClose = gmtClose;
            return this;
        }

        public Date getGmtEnd() {
            return gmtEnd;
        }

        public DepositOrderBuilder setGmtEnd(Date gmtEnd) {
            this.gmtEnd = gmtEnd;
            return this;
        }

        public Date getGmtDeadline() {
            return gmtDeadline;
        }

        public DepositOrderBuilder setGmtDeadline(Date gmtDeadline) {
            this.gmtDeadline = gmtDeadline;
            return this;
        }

        public Date getGmtCreate() {
            return gmtCreate;
        }

        public DepositOrderBuilder setGmtCreate(Date gmtCreate) {
            this.gmtCreate = gmtCreate;
            return this;
        }

        public Date getGmtModified() {
            return gmtModified;
        }

        public DepositOrderBuilder setGmtModified(Date gmtModified) {
            this.gmtModified = gmtModified;
            return this;
        }

        public DepositOrderBO build(){
            return new DepositOrderBO(this);
        }

    }

    public DepositOrder getDepositOrder() {
        return depositOrder;
    }
}
