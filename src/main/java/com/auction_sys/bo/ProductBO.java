package com.auction_sys.bo;

import com.auction_sys.pojo.Product;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created By liuda on 2018/7/9
 */
public class ProductBO {

    private Product product;
    private ProductBO(ProductBuilder builder){
        product = new Product(builder);
    }

    public Product getProduct() {
        return product;
    }

    public static class ProductBuilder{

        private Long id;

        private Integer schoolId;

        private String name;

        private String subTitle;

        private Integer categoryId;

        private Date gmtDeadline;

        private Long sellerId;

        private BigDecimal cashDeposit;

        private BigDecimal scale;

        private BigDecimal startingPrice;

        private BigDecimal currentPrice;

        private BigDecimal reservePrice;

        private String mainImage;

        private String subImage;

        private String detail;

        private Byte status;

        private Byte isDeleted;

        private Date gmtCreate;

        private Date gmtModified;

        public Long getId() {
            return id;
        }

        public ProductBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public Integer getSchoolId() {
            return schoolId;
        }

        public ProductBuilder setSchoolId(Integer schoolId) {
            this.schoolId = schoolId;
            return this;
        }

        public String getName() {
            return name;
        }

        public ProductBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public ProductBuilder setSubTitle(String subTitle) {
            this.subTitle = subTitle;
            return this;
        }

        public Integer getCategoryId() {
            return categoryId;
        }

        public ProductBuilder setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Date getGmtDeadline() {
            return gmtDeadline;
        }

        public ProductBuilder setGmtDeadline(Date gmtDeadline) {
            this.gmtDeadline = gmtDeadline;
            return this;
        }

        public Long getSellerId() {
            return sellerId;
        }

        public ProductBuilder setSellerId(Long sellerId) {
            this.sellerId = sellerId;
            return this;
        }

        public BigDecimal getCashDeposit() {
            return cashDeposit;
        }

        public ProductBuilder setCashDeposit(BigDecimal cashDeposit) {
            this.cashDeposit = cashDeposit;
            return this;
        }

        public BigDecimal getScale() {
            return scale;
        }

        public ProductBuilder setScale(BigDecimal scale) {
            this.scale = scale;
            return this;
        }

        public BigDecimal getStartingPrice() {
            return startingPrice;
        }

        public ProductBuilder setStartingPrice(BigDecimal startingPrice) {
            this.startingPrice = startingPrice;
            return this;
        }

        public BigDecimal getCurrentPrice() {
            return currentPrice;
        }

        public ProductBuilder setCurrentPrice(BigDecimal currentPrice) {
            this.currentPrice = currentPrice;
            return this;
        }

        public BigDecimal getReservePrice() {
            return reservePrice;
        }

        public ProductBuilder setReservePrice(BigDecimal reservePrice) {
            this.reservePrice = reservePrice;
            return this;
        }

        public String getMainImage() {
            return mainImage;
        }

        public ProductBuilder setMainImage(String mainImage) {
            this.mainImage = mainImage;
            return this;
        }

        public String getSubImage() {
            return subImage;
        }

        public ProductBuilder setSubImage(String subImage) {
            this.subImage = subImage;
            return this;
        }

        public String getDetail() {
            return detail;
        }

        public ProductBuilder setDetail(String detail) {
            this.detail = detail;
            return this;
        }

        public Byte getStatus() {
            return status;
        }

        public ProductBuilder setStatus(Byte status) {
            this.status = status;
            return this;
        }

        public Byte getIsDeleted() {
            return isDeleted;
        }

        public ProductBuilder setIsDeleted(Byte isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public Date getGmtCreate() {
            return gmtCreate;
        }

        public ProductBuilder setGmtCreate(Date gmtCreate) {
            this.gmtCreate = gmtCreate;
            return this;
        }

        public Date getGmtModified() {
            return gmtModified;
        }

        public ProductBuilder setGmtModified(Date gmtModified) {
            this.gmtModified = gmtModified;
            return this;
        }

        public ProductBO build(){
            return new ProductBO(this);
        }


    }

}
