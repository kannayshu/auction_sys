package com.auction_sys.service;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.pojo.DepositOrder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created By liuda on 2018/7/9
 */
public interface OrderService {

     ServerResponse checkBid(long productId , long userId,BigDecimal payment);
     ServerResponse createOrder(long productId , long userId,BigDecimal payment,long shippingId);

     ServerResponse bidderDepositPay(Long orderId, Long userId);

     ServerResponse sellerDepositPay(Long depositId, Long userId);

     ServerResponse createDepositOrderForSeller(Long productId, Long userId);

     ServerResponse aliCallback(Map<String, String> params);

     ServerResponse depositRefund(Long orderId, Long userId);

     ServerResponse depositRefund(DepositOrder order);

     ServerResponse productOrderFianlPay(Long orderId, Long userId);

     ServerResponse sellerDeliverGoods(Long productId, String logisticsNumber, String logisticsCompany, Long userId);
     ServerResponse comfirmReceipt(Long orderId,Long bidderId);
     ServerResponse orderCompleteDepositRefundToSeller(DepositOrder depositOrder);
     ServerResponse getPaymentStatus(Long orderId,Long userId);
}
