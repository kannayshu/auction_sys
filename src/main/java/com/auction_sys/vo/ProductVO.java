package com.auction_sys.vo;

import com.auction_sys.common.vaild.BigDecimalNonnegative;
import com.auction_sys.common.vaild.NotNull;
import com.auction_sys.common.vaild.Null;
import com.auction_sys.common.vaild.VaildType;
import com.auction_sys.pojo.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created By liuda on 2018/6/3
 */
public class ProductVO {

    private Long productId;

    private String name;
    private String subTitle;
    private Integer categoryId;
    private BigDecimal cashDeposit;
    private BigDecimal currentPrice;
    private BigDecimal bidPrice;
    private String mainImage;
    public ProductVO(Product p){
       name = p.getName();
       subTitle = p.getSubTitle();
       categoryId =  p.getCategoryId();
       cashDeposit = p.getCashDeposit();
       currentPrice = p.getCurrentPrice();
       mainImage = p.getMainImage();
   }

    public Long getProductId() {
        return productId;
    }

    public ProductVO setProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductVO setName(String name) {
        this.name = name;
        return this;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public ProductVO setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public ProductVO setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public BigDecimal getCashDeposit() {
        return cashDeposit;
    }

    public ProductVO setCashDeposit(BigDecimal cashDeposit) {
        this.cashDeposit = cashDeposit;
        return this;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public ProductVO setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
        return this;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public ProductVO setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
        return this;
    }

    public String getMainImage() {
        return mainImage;
    }

    public ProductVO setMainImage(String mainImage) {
        this.mainImage = mainImage;
        return this;
    }
}
