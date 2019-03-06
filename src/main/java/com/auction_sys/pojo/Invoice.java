package com.auction_sys.pojo;

import java.util.Date;

public class Invoice {
    private Long invoiceId;

    private Long productOrderId;

    private String receiverPhone;

    private String shippingAddress;

    private String receiverZip;

    private String receiverName;

    private String logisticsNumber;

    private String logisticsCompany;

    private Byte isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    public Invoice(Long invoiceId, Long productOrderId, String receiverPhone, String shippingAddress, String receiverZip, String receiverName, String logisticsNumber, String logisticsCompany,  Byte isDeleted, Date gmtCreate, Date gmtModified) {
        this.invoiceId = invoiceId;
        this.productOrderId = productOrderId;
        this.receiverPhone = receiverPhone;
        this.shippingAddress = shippingAddress;
        this.receiverZip = receiverZip;
        this.receiverName = receiverName;
        this.logisticsNumber = logisticsNumber;
        this.logisticsCompany = logisticsCompany;
        this.isDeleted = isDeleted;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public Invoice() {
        super();
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Long getProductOrderId() {
        return productOrderId;
    }

    public void setProductOrderId(Long productOrderId) {
        this.productOrderId = productOrderId;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone == null ? null : receiverPhone.trim();
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getReceiverZip() {
        return receiverZip;
    }

    public void setReceiverZip(String receiverZip) {
        this.receiverZip = receiverZip == null ? null : receiverZip.trim();
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName == null ? null : receiverName.trim();
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber == null ? null : logisticsNumber.trim();
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany == null ? null : logisticsCompany.trim();
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
}