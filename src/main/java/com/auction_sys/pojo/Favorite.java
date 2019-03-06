package com.auction_sys.pojo;

import java.util.Date;

public class Favorite {
    private Long favId;

    private Long favProductId;

    private Long userId;

    private Byte isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    public Favorite(Long favId, Long favProductId, Long userId, Byte isDeleted, Date gmtCreate, Date gmtModified) {
        this.favId = favId;
        this.favProductId = favProductId;
        this.userId = userId;
        this.isDeleted = isDeleted;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public Favorite() {
        super();
    }

    public Long getFavId() {
        return favId;
    }

    public Favorite setFavId(Long favId) {
        this.favId = favId;
        return this;
    }

    public Long getFavProductId() {
        return favProductId;
    }

    public Favorite setFavProductId(Long favProductId) {
        this.favProductId = favProductId;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public Favorite setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public Favorite setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
        return this;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public Favorite setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
        return this;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public Favorite setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
        return this;
    }
}