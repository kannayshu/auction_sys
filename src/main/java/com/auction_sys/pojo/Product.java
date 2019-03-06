package com.auction_sys.pojo;

import com.auction_sys.bo.ProductBO;
import com.auction_sys.common.vaild.BigDecimalNonnegative;
import com.auction_sys.common.vaild.NotNull;
import com.auction_sys.common.vaild.Null;
import com.auction_sys.common.vaild.VaildType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;

@JsonSerialize(include =  JsonSerialize.Inclusion.NON_NULL)
public class Product {
    @Null(VaildType.INSERT)
    @NotNull(VaildType.UPDATE)
    private Long productId;


    private Integer schoolId;

    @NotNull(VaildType.INSERT)
    private String name;
    @NotNull(VaildType.INSERT)
    private String subTitle;
    @NotNull(VaildType.INSERT)
    private Integer categoryId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(VaildType.INSERT)
    private Date gmtDeadline;


    @Null(VaildType.UPDATE|VaildType.INSERT)
    private Long sellerId;

    @NotNull(VaildType.INSERT)
    @BigDecimalNonnegative
    private BigDecimal cashDeposit;
    @NotNull(VaildType.INSERT)
    @BigDecimalNonnegative
    private BigDecimal scale;
    @NotNull(VaildType.INSERT)
    @BigDecimalNonnegative
    private BigDecimal startingPrice;
    @Null(VaildType.UPDATE|VaildType.INSERT)
    private BigDecimal currentPrice;
    @NotNull(VaildType.INSERT)
    @BigDecimalNonnegative
    private BigDecimal reservePrice;

    @NotNull(VaildType.INSERT)
    private String mainImage;

    @NotNull(VaildType.INSERT)
    private String subImage;

    @NotNull(VaildType.INSERT)
    private String detail;

    @Null(VaildType.UPDATE|VaildType.INSERT)
    private Byte status;

    @Null(VaildType.UPDATE|VaildType.INSERT)
    @JsonIgnore
    private Byte isDeleted;

    private Date gmtCreate;

    private Date gmtModified;
    public Product( Long sellerId , Byte status) {
        this.sellerId = sellerId;
        this.status = status;
    }
    public Product(Long id, Integer schoolId, String name, String subTitle, Integer categoryId, Date gmtDeadline, Long sellerId, BigDecimal scale, BigDecimal cashDeposit, BigDecimal startingPrice, BigDecimal currentPrice, BigDecimal reservePrice, String mainImage, String subImage, Byte status, Date gmtCreate, Date gmtModified) {
        this.productId = id;
        this.schoolId = schoolId;
        this.name = name;
        this.subTitle = subTitle;
        this.categoryId = categoryId;
        this.sellerId = sellerId;
        this.scale = scale;
        this.startingPrice = startingPrice;
        this.currentPrice = currentPrice;
        this.reservePrice = reservePrice;
        this.mainImage = mainImage;
        this.subImage = subImage;
        this.status = status;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
        this.cashDeposit = cashDeposit;
        this.gmtDeadline = gmtDeadline;
    }
    public Product(ProductBO.ProductBuilder builder) {
        this.productId = builder.getId();
        this.schoolId = builder.getSchoolId();
        this.name = builder.getName();
        this.subTitle = builder.getSubTitle();
        this.categoryId = builder.getCategoryId();
        this.sellerId = builder.getSellerId();
        this.scale = builder.getScale();
        this.startingPrice =builder.getStartingPrice();
        this.currentPrice = builder.getCurrentPrice();
        this.reservePrice = builder.getReservePrice();
        this.mainImage = builder .getMainImage();
        this.subImage = builder.getSubImage();
        this.status = builder.getStatus();
        this.gmtCreate = builder.getGmtCreate();
        this.gmtModified = builder.getGmtModified();
        this.cashDeposit = builder.getCashDeposit();
        this.gmtDeadline = builder.getGmtDeadline();
        this.detail = builder.getDetail();
    }
    public Product(Long id, Integer schoolId, String name, String subTitle, Integer categoryId, Date gmtDeadline, Long sellerId, BigDecimal scale, BigDecimal cashDeposit, BigDecimal startingPrice, BigDecimal currentPrice, BigDecimal reservePrice, String mainImage, String subImage, String detail, Byte status, Date gmtCreate, Date gmtModified) {


        this.productId = id;
        this.schoolId = schoolId;
        this.name = name;
        this.subTitle = subTitle;
        this.categoryId = categoryId;
        this.sellerId = sellerId;
        this.scale = scale;
        this.startingPrice = startingPrice;
        this.currentPrice = currentPrice;
        this.reservePrice = reservePrice;
        this.mainImage = mainImage;
        this.subImage = subImage;
        this.detail = detail;
        this.status = status;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
        this.cashDeposit = cashDeposit;
        this.gmtDeadline = gmtDeadline;
    }
    public Product(Long id, Integer schoolId, String name, String subTitle, Integer categoryId, Date gmtDeadline, BigDecimal cashDeposit, BigDecimal startingPrice, BigDecimal currentPrice, BigDecimal reservePrice, String mainImage, String subImage, Byte status) {


        this.productId = id;
        this.schoolId = schoolId;
        this.name = name;
        this.subTitle = subTitle;
        this.categoryId = categoryId;
        this.startingPrice = startingPrice;
        this.currentPrice = currentPrice;
        this.reservePrice = reservePrice;
        this.mainImage = mainImage;
        this.subImage = subImage;
        this.status = status;
        this.cashDeposit = cashDeposit;
        this.gmtDeadline = gmtDeadline;
    }
    public Product() {
        super();
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long id) {
        this.productId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle == null ? null : subTitle.trim();
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public BigDecimal getScale() {
        return scale;
    }

    public void setScale(BigDecimal scale) {
        this.scale = scale;
    }

    public BigDecimal getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(BigDecimal startingPrice) {
        this.startingPrice = startingPrice;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public BigDecimal getReservePrice() {
        return reservePrice;
    }

    public void setReservePrice(BigDecimal reservePrice) {
        this.reservePrice = reservePrice;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage == null ? null : mainImage.trim();
    }

    public String getSubImage() {
        return subImage;
    }

    public void setSubImage(String subImage) {
        this.subImage = subImage == null ? null : subImage.trim();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
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

    public BigDecimal getCashDeposit() {
        return cashDeposit;
    }

    public void setCashDeposit(BigDecimal cashDeposit) {
        this.cashDeposit = cashDeposit;
    }

    public Date getGmtDeadline() {
        return gmtDeadline;
    }


    public void setGmtDeadline(Date gmtDeadline) {
        this.gmtDeadline = gmtDeadline;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", schoolId=" + schoolId +
                ", name='" + name + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", categoryId=" + categoryId +
                ", gmtDeadline=" + gmtDeadline +
                ", sellerId=" + sellerId +
                ", cashDeposit=" + cashDeposit +
                ", scale=" + scale +
                ", startingPrice=" + startingPrice +
                ", currentPrice=" + currentPrice +
                ", reservePrice=" + reservePrice +
                ", mainImage='" + mainImage + '\'' +
                ", subImage='" + subImage + '\'' +
                ", detail='" + detail + '\'' +
                ", status=" + status +
                ", isDeleted=" + isDeleted +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}