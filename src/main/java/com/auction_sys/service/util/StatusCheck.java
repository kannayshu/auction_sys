package com.auction_sys.service.util;

import com.auction_sys.common.ServerResponse;

/**
 * Created By liuda on 2018/5/26
 */
public interface StatusCheck<T> {
    ServerResponse<T> check(byte status);
    boolean checkActive(byte status);
    boolean checkLimit(byte status);
}
