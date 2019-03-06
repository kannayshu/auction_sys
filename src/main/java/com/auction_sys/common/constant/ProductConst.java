package com.auction_sys.common.constant;

/**
 * Created By liuda on 2018/7/11
 */
public class ProductConst {
    public interface ProductResponStateConst {
        int PRODUCT_INEXISTENCE = 40011;
    }


    public interface ProductStatusConst{
        byte DRAFT_STATUS = 1;
        byte DEPOSIT_PAYED = 2;
        byte BIDDING = 3;
        byte TIME_OVER = 4;
        byte COMPLETED = 5;
    }
}
