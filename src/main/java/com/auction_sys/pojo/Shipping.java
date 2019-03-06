package com.auction_sys.pojo;

import com.auction_sys.common.vaild.NotNull;
import com.auction_sys.common.vaild.Null;
import com.auction_sys.common.vaild.VaildType;

import java.util.Date;

public class Shipping {


    @NotNull(VaildType.UPDATE)
    private Long shippingId;
    @Null
    private Long userId;
    @NotNull
    private String receiverName;

    @NotNull
    private String receiverPhone;
    @NotNull
    private String receiverProvince;
    @NotNull
    private String receiverCity;
    @NotNull
    private String receiverDistrict;
    @NotNull
    private String receiverAddress;
    @NotNull
    private String receiverZip;
    private Date gmtCreate;
    private Date gmtModified;
    public Shipping(Long shippingId, Long userId, String receiverName, String receiverPhone, String receiverProvince, String receiverCity, String receiverDistrict, String receiverAddress, String receiverZip, Date gmtCreate, Date gmtModified) {
        this.shippingId = shippingId;
        this.userId = userId;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverProvince = receiverProvince;
        this.receiverCity = receiverCity;
        this.receiverDistrict = receiverDistrict;
        this.receiverAddress = receiverAddress;
        this.receiverZip = receiverZip;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public Shipping() {
        super();
    }

    public Long getShippingId() {
        return shippingId;
    }

    public void setShippingId(Long shippingId) {
        this.shippingId = shippingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName == null ? null : receiverName.trim();
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone == null ? null : receiverPhone.trim();
    }

    public String getReceiverProvince() {
        return receiverProvince;
    }

    public void setReceiverProvince(String receiverProvince) {
        this.receiverProvince = receiverProvince == null ? null : receiverProvince.trim();
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity == null ? null : receiverCity.trim();
    }

    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict == null ? null : receiverDistrict.trim();
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress == null ? null : receiverAddress.trim();
    }

    public String getReceiverZip() {
        return receiverZip;
    }

    public void setReceiverZip(String receiverZip) {
        this.receiverZip = receiverZip == null ? null : receiverZip.trim();
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