package com.auction_sys.pojo;

import java.util.Date;

public class PayInfo {
    private Long payInfoId;

    private Long userId;

    private Long orderId;

    private Byte payPlatform;

    private String buyerLogonId;

    private String platformNumber;

    private String platformStatus;

    private Byte type;

    private Byte isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    public PayInfo(Long payInfoId, Long userId, String buyerLogonId,Long orderId, Byte payPlatform, String platformNumber, String platformStatus, Byte type, Byte isDeleted, Date gmtCreate, Date gmtModified) {
        this.payInfoId = payInfoId;
        this.userId = userId;
        this.orderId = orderId;
        this.buyerLogonId = buyerLogonId;
        this.payPlatform = payPlatform;
        this.platformNumber = platformNumber;
        this.platformStatus = platformStatus;
        this.type = type;
        this.isDeleted = isDeleted;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public PayInfo() {
        super();
    }

    public Long getPayInfoId() {
        return payInfoId;
    }

    public void setPayInfoId(Long payInfoId) {
        this.payInfoId = payInfoId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Byte getPayPlatform() {
        return payPlatform;
    }

    public void setPayPlatform(Byte payPlatform) {
        this.payPlatform = payPlatform;
    }

    public String getPlatformNumber() {
        return platformNumber;
    }

    public void setPlatformNumber(String platformNumber) {
        this.platformNumber = platformNumber == null ? null : platformNumber.trim();
    }

    public String getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(String platformStatus) {
        this.platformStatus = platformStatus == null ? null : platformStatus.trim();
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
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

    public String getBuyerLogonId() {
        return buyerLogonId;
    }

    public void setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId;
    }
}