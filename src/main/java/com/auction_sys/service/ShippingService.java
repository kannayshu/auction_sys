package com.auction_sys.service;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.pojo.Shipping;
import com.auction_sys.pojo.User;

/**
 * Created By liuda on 2018/7/18
 */
public interface ShippingService {
    public ServerResponse addShipping(Shipping shipping, User user);
    public ServerResponse updateShipping(Shipping shipping, User user);
    public ServerResponse getUsersShipping(User user);
    public ServerResponse deleteShipping(Long shippingId, User user);

    public ServerResponse getShipping(User user, Long id);
}
