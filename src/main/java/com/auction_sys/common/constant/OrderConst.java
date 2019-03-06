package com.auction_sys.common.constant;

/**
 * Created By liuda on 2018/7/11
 */
public class OrderConst {

    public interface OrderResponStateConst {


        int DEPOSIT_ORDDER_INEXISTENCE = 30011;

        int PRODUCT_ORDDER_INEXISTENCE = 30012;

        int PRODUCT_INEXISTENCE = 30013;

        int CORRESPONDING_PRODUCT_INEXISTENCE = 30021;

        int PRE_ORDER_FAILED = 30022;

        int PRE_ORDER_UNKNOWN = 30023;

        int REFUND_FAILED = 30024;

        int REFUND_UNKNOWN = 30025;

        int REPETITION_OPERATION = 30026;

        int DEPOSIT_PAYED = 30027;
        int DEPOSIT_UNPAYED = 30028;

    }


    //数据库中产品订单状态
    public interface ProductOrderStatusConst {


        //等待支付定金
        byte WAIT_TO_PAY_DEPOSIT = 1;
        //等待截至时间
        byte BIDDING = 2;
        //等待重新出价
        byte WAIT_TO_REBID = 3;
        //已取消
        byte CANCELED = 4;
        //未支付
        byte WAIT_FINAL_PAY= 5;
        //已付款
        byte WAIT_TO_COMPLETE_SHIPMENTS_INFO = 6;
        //等待卖家发货
        byte WAIT_SELLER_DELIVER_GOODS = 7;
        //等待竞标者收货
        byte WAIT_BIDDER_RECEIVER = 8;
        //交易成功
        byte WAIT_PAY_TO_SELLER = 9;
        //竞标者违约
        byte BIDDER_BREAK_CONTACT = 10;
        //卖家违约
        byte SELLER_BREAK_CONTACT = 11;
        //交易成功
        byte ORDER_COMPLETED = 12;
        //订单关闭
        byte ORDER_CLOSED = 0;
    }

    //数据库中定金订单状态
    public interface DepositOrderStatusConst{
        //等待支付定金
        byte WAIT_TO_PAY = 1;
        //竞标中
        byte BIDDING = 2;
        //允许退款
        byte CAN_REFUMD = 3;
        //申请退款中
        byte REFUNDING = 4;
        byte WAIT_ORDER_COMPLETED = 5;
        //订单关闭
        byte ORDER_COMPLETED = 6;

        byte ORDER_CLOSED = 0;
    }

    public interface OrderPaymentTypeConst{
        byte ONLINE_PAYMENT = 1;
    }

    public interface DepositOrderTypeConst{
        byte BIDDER = 1;
        byte SELLER = 2;
    }
    public interface OrderTypeConst{
        byte DEPOSIT_ORDER = 1;
        byte PRODUCT_ORDER = 2;
    }


    public interface PayPlatformConst{
        //支付宝
        byte ALIPAY = 1;

    }

    public interface  AlipayCallbackConst{
        //	交易创建，等待买家付款
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        //交易支付成功
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";
        //	未付款交易超时关闭，或支付完成后全额退款
        String TRADE_STATUS_RADE_CLOSED = "TRADE_CLOSED";
        //交易结束，不可退款
        String TRADE_STATUS_TRADE_FINISHED = "TRADE_FINISHED";
        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    public interface TimeoutConst{
        int WAIT_BIDDER_RECEIPT_TIME = 7*24*60*60*1000;
        int WAIT_BIDDER_FINAL_PAY_TIME = 7*24*60*60*1000;
        int WAIT_REBID_TIME = 1*24*60*60*1000;
    }
}
