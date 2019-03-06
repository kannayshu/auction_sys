package com.auction_sys.pojo;

import java.util.Date;

public class Chance {
    private Long chanceId;

    private Long productId;

    private Long bidderId;

    private Byte chanceTime;

    private Byte status;

    private Date gmtCreate;

    private Date gmtModified;

    public Chance(Long chanceId, Long productId, Long bidderId, Byte chanceTime, Byte status, Date gmtCreate, Date gmtModified) {
        this.chanceId = chanceId;
        this.productId = productId;
        this.bidderId = bidderId;
        this.chanceTime = chanceTime;
        this.status = status;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public Chance() {
        super();
    }

    public Long getChanceId() {
        return chanceId;
    }

    public void setChanceId(Long chanceId) {
        this.chanceId = chanceId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getBidderId() {
        return bidderId;
    }

    public void setBidderId(Long bidderId) {
        this.bidderId = bidderId;
    }

    public Byte getChanceTime() {
        return chanceTime;
    }

    public void setChanceTime(Byte chanceTime) {
        this.chanceTime = chanceTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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