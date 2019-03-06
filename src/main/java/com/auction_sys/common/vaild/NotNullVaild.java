package com.auction_sys.common.vaild;

/**
 * Created By liuda on 2018/7/17
 */
public class NotNullVaild extends Vaild {

    @Override
    boolean insertable(Object boj) {
        return false;
    }

    @Override
    boolean updateable(Object boj) {
        return false;
    }
}
