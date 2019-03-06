package com.auction_sys.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradeRefundRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.auction_sys.bo.DepositOrderBO;
import com.auction_sys.bo.ProductOrderBO;
import com.auction_sys.common.ServerResponse;
import com.auction_sys.common.constant.*;
import com.auction_sys.dao.*;
import com.auction_sys.exception.DatabaseUpdateErrorExecption;
import com.auction_sys.pojo.*;
import com.auction_sys.service.OrderService;
import com.auction_sys.util.DateTimeUtil;
import com.auction_sys.util.FTPUtil;
import com.auction_sys.util.PropertiesUtil;
import com.auction_sys.vo.OrderVO;
import com.auction_sys.vo.ProductVO;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created By liuda on 2018/7/9
 */
@Component
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private static final String QR_PATH = "qr_image";
    private static final int PAY_TIMEOUT = 30*60*1000;
    @Autowired
    DepositOrderMapper depositOrderMapper;
    @Autowired
    ProductOrderMapper productOrderMapper;
    @Autowired
    PayInfoMapper payInfoMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    ShippingMapper shippingMapper;
    @Autowired
    InvoiceMapper invoiceMapper;
    @Autowired
    OrderService orderService;


    private static AlipayTradeService tradeService;
    static {
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }


    @Transactional
    public ServerResponse rebid(ProductOrder productOrder , long userId,BigDecimal payment){


        if (productOrder.getBidderId()!=userId)
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.ILLEGAL_PARAMETETS);

        ProductOrder lastOrder = productOrderMapper.selectOrderByProductIdAndStatus(productOrder.getProductId(),
                OrderConst.ProductOrderStatusConst.BIDDING);

        DepositOrder depositOrder = new DepositOrder();
        depositOrder.setDepositId(productOrder.getDepositId());
        depositOrder.setStatus(OrderConst.DepositOrderStatusConst.BIDDING);
        int num = depositOrderMapper.updateByPrimaryKeySelective(depositOrder);
        ProductOrder order = new ProductOrder();
        order.setOrderId(productOrder.getOrderId());
        order.setPayment(payment);
        order.setStatus(OrderConst.ProductOrderStatusConst.BIDDING);
        num+= productOrderMapper.updateByPrimaryKeySelective(order);
        if (num>1){
            Product product = productMapper.selectSimpleResultByPrimaryKey(productOrder.getProductId());
            if (lastOrder!=null&&!lastOrder.getOrderId().equals(productOrder.getOrderId())){
                changeOtherOrder(lastOrder);
            }
            updateProductPayment(productOrder.getDepositId()+1,product.getProductId());
            return ServerResponse.createBySuccess();
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return ServerResponse.createByError();
    }

      //检查用户是否第一次竞拍该商品
    public ServerResponse checkBid(long productId , long userId,BigDecimal payment){
        //查找并检查商品是否存在
        Product product  = productMapper.selectSimpleResultByPrimaryKey(productId);
        BigDecimal scala = payment.subtract(product.getCurrentPrice());
        ProductVO productVO = new ProductVO(product);
        productVO.setBidPrice(payment);
        if (scala.compareTo(product.getScale())<0)
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.ILLEGAL_PARAMETETS);
        //检查商品状态
        if(product==null||product.getStatus()!=ProductConst.ProductStatusConst.BIDDING)
            return ServerResponse.createByError(ProductConst.ProductResponStateConst.PRODUCT_INEXISTENCE);
        //查看是否已经有过拍卖纪录,若处于正在拍卖或正在等待重新拍卖状态则转入rebid处理
        ProductOrder productOrder = productOrderMapper.selectOrderByProductIdAndUserId(productId,userId);
        if (productOrder!=null&&(productOrder.getStatus()== OrderConst.ProductOrderStatusConst.WAIT_TO_REBID||
                productOrder.getStatus()==  OrderConst.ProductOrderStatusConst.BIDDING))
            //如果用户是二次竞拍，则发送用户已经支付过定金的消息
            return ServerResponse.createBySuccess(OrderConst.OrderResponStateConst.DEPOSIT_PAYED,product);
        if (productOrder!=null&&productOrder.getStatus()== OrderConst.ProductOrderStatusConst.WAIT_TO_PAY_DEPOSIT)
            return ServerResponse.createByError(OrderConst.OrderResponStateConst.REPETITION_OPERATION);
        return ServerResponse.createBySuccess(OrderConst.OrderResponStateConst.DEPOSIT_UNPAYED,product);

    }


    public ServerResponse createOrder(long productId , long userId,BigDecimal payment,long shippingId){

        //查找并检查商品是否存在
        Product product  = productMapper.selectSimpleResultByPrimaryKey(productId);
        BigDecimal scala = payment.subtract(product.getCurrentPrice());
        if (scala.compareTo(product.getScale())<0)
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.ILLEGAL_PARAMETETS);
        //检查商品状态
        if(product==null||product.getStatus()!=ProductConst.ProductStatusConst.BIDDING)
            return ServerResponse.createByError(ProductConst.ProductResponStateConst.PRODUCT_INEXISTENCE);
        //查看是否已经有过拍卖纪录,若处于正在拍卖或正在等待重新拍卖状态则转入rebid处理
        ProductOrder productOrder = productOrderMapper.selectOrderByProductIdAndUserId(productId,userId);
        if (productOrder!=null&&(productOrder.getStatus()== OrderConst.ProductOrderStatusConst.WAIT_TO_REBID||
                productOrder.getStatus()==  OrderConst.ProductOrderStatusConst.BIDDING))
            return rebid(productOrder,userId,payment);
        if (productOrder!=null&&productOrder.getStatus()== OrderConst.ProductOrderStatusConst.WAIT_TO_PAY_DEPOSIT)
            return ServerResponse.createByError(OrderConst.OrderResponStateConst.REPETITION_OPERATION);

        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        if (shipping==null)
            return ServerResponse.createByError(ShippingConst.ShippingResponStateConst.SHIPPING_INEXISTENCE);
        if (shipping.getUserId()!=userId)
            return ServerResponse.createByError(ShippingConst.ShippingResponStateConst.SHIPPING_INEXISTENCE);

        //用当前时间戳表示订单号，最后一位为0表示为定金订单
        long currentTime = System.currentTimeMillis();
        long depoOrdId = currentTime*10;
        DepositOrderBO.DepositOrderBuilder depositOrderBuilder = new DepositOrderBO.DepositOrderBuilder();

        DepositOrderBO depositOrderBO = depositOrderBuilder.setDepositId(depoOrdId)
                .setPayerId(userId).setProductId(product.getProductId())
                .setPayment(product.getCashDeposit()).setPaymentType(OrderConst.OrderPaymentTypeConst.ONLINE_PAYMENT)
                .setStatus(OrderConst.DepositOrderStatusConst.WAIT_TO_PAY).setGmtDeadline(new Date(System.currentTimeMillis()+PAY_TIMEOUT))
                .setType(OrderConst.DepositOrderTypeConst.BIDDER).build();
        int num0 = depositOrderMapper.insert(depositOrderBO.getDepositOrder());
        if(num0<=0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
        }

        //用当前时间戳表示订单号，最后一位为1表示为产品订单
        long prodOrdId = currentTime*10+1;
        ProductOrderBO.ProductOrderBuilder productOrderBuilder = new ProductOrderBO.ProductOrderBuilder();
        ProductOrderBO productOrderBO = productOrderBuilder.setOrderId(prodOrdId).setProductId(product.getProductId()).setDepositId(depoOrdId)
                .setPayment(payment).setBidderId(userId)
                .setGmtDeadline(new Date(System.currentTimeMillis()+PAY_TIMEOUT))
                .setPaymentType(OrderConst.OrderPaymentTypeConst.ONLINE_PAYMENT).setStatus(OrderConst.ProductOrderStatusConst.WAIT_TO_PAY_DEPOSIT).build();
        int num1 = productOrderMapper.insert(productOrderBO.getProductOrder());
        if(num1<=0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
        }
        OrderVO orderVO = new OrderVO().setDepositOrderId(depoOrdId).setProductOrderId(productId).
                setBidderId(userId).setCashDeposit(product.getCashDeposit()).setProduct(product).
                setProductPayment(product.getCurrentPrice()).setGmtCreate(new Date(System.currentTimeMillis()));
        ServerResponse serverResponse = ServerResponse.createBySuccess(orderVO);

        if (serverResponse.isSuccess()){
            Invoice invoice = new Invoice(null,prodOrdId,shipping.getReceiverPhone(),
                    shipping.getReceiverProvince()+shipping.getReceiverCity()+shipping.getReceiverDistrict()+shipping.getReceiverAddress(),
                    shipping.getReceiverZip(),shipping.getReceiverName(),null,null,null,null,null);
            int num = invoiceMapper.insertSelective(invoice);
            if (num<=0)
                logger.error("发货单更新出错");
        }
        return serverResponse;
    }


    public ServerResponse bidderDepositPay(Long orderId, Long userId){

        DepositOrder order = depositOrderMapper.selectByUserIdAndDepositId(userId, orderId);
        if(order == null){
            return ServerResponse.createByError(OrderConst.OrderResponStateConst.DEPOSIT_ORDDER_INEXISTENCE);
        }
        if (order.getStatus()!=OrderConst.DepositOrderStatusConst.WAIT_TO_PAY)
             return ServerResponse.createByError(CommonConst.CommonResponStateConst.ILLEGAL_PARAMETETS);
        Product product = productMapper.selectSimpleResultByPrimaryKey(order.getProductId());
        if (product==null)
            return  ServerResponse.createByError(OrderConst.OrderResponStateConst.DEPOSIT_ORDDER_INEXISTENCE);
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getDepositId().toString();


        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append(product.getName()).append("定金扫码支付,定金订单号:").append(outTradeNo).toString();


        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();


        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = totalAmount;



        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单").append(outTradeNo).append("拍品支付需支付定金").append(totalAmount).append("元").toString();


        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
//        ExtendParams extendParams = new ExtendParams();
//        extendParams.setSysServiceProviderId("2088100200300400500");


        // 支付超时，定义为5分钟
        String timeoutExpress = "5m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>(1);

        GoodsDetail goods = GoodsDetail.newInstance(product.getProductId().toString(), product.getName(),
                    product.getCashDeposit().multiply(new BigDecimal(100)).longValue(),1);
            goodsDetailList.add(goods);


        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);
        ServerResponse serverResponse = alipayTradePreOrder(builder,order.getDepositId());

        return serverResponse;
    }

    public  ServerResponse createDepositOrderForSeller(Long productId,Long userId) {

        Product product = productMapper.selectSimpleResultByPrimaryKey(productId);
        //判断商品状态是否合法
        if (product == null || product.getStatus() != ProductConst.ProductStatusConst.DRAFT_STATUS)
            return ServerResponse.createByErrorIllegalParam();
        if (product.getSellerId() != userId)
            return ServerResponse.createByErrorIllegalOperation();
        //判断是否已经存在未支付的订单
        DepositOrder order = depositOrderMapper.selectDepositByPayerIdAndTypeAndProductId(userId, OrderConst.DepositOrderTypeConst.SELLER, productId);
        if (order != null && order.getStatus() != OrderConst.DepositOrderStatusConst.ORDER_CLOSED)
            return ServerResponse.createByError(OrderConst.OrderResponStateConst.REPETITION_OPERATION);
        if (order == null)
            return ServerResponse.createByError(OrderConst.OrderResponStateConst.DEPOSIT_ORDDER_INEXISTENCE);

        long currentTime = System.currentTimeMillis();
        long depoOrdId = currentTime * 10;
        DepositOrderBO.DepositOrderBuilder depositOrderBuilder = new DepositOrderBO.DepositOrderBuilder();
        DepositOrderBO depositOrderBO = depositOrderBuilder.setDepositId(depoOrdId)
                .setPayerId(userId).setProductId(product.getProductId())
                .setPayment(product.getCashDeposit()).setPaymentType(OrderConst.OrderPaymentTypeConst.ONLINE_PAYMENT)
                .setStatus(OrderConst.DepositOrderStatusConst.WAIT_TO_PAY).setType(OrderConst.DepositOrderTypeConst.SELLER)
                .setGmtDeadline(new Date(System.currentTimeMillis() + PAY_TIMEOUT)).build();
        int depositId = depositOrderMapper.insert(depositOrderBO.getDepositOrder());
        if (depositId <= 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
        }
        return ServerResponse.createBySuccessOperationSuccess(depoOrdId);
    }

    public ServerResponse sellerDepositPay(Long depositId,Long userId){
        DepositOrder order = depositOrderMapper.selectByUserIdAndDepositId(userId, depositId);
        if(order == null){
            return ServerResponse.createByError(OrderConst.OrderResponStateConst.DEPOSIT_ORDDER_INEXISTENCE);
        }
        if (order.getStatus()!=OrderConst.DepositOrderStatusConst.WAIT_TO_PAY)
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.ILLEGAL_PARAMETETS);
        Product product = productMapper.selectSimpleResultByPrimaryKey(order.getProductId());
        if (product==null)
            return  ServerResponse.createByError(OrderConst.OrderResponStateConst.DEPOSIT_ORDDER_INEXISTENCE);
               // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getDepositId().toString();
        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append(product.getName()).append("卖家定金扫码支付,定金订单号:").append(outTradeNo).toString();


        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();


        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = totalAmount;



        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单").append(outTradeNo).append("拍品上架需支付定金").append(totalAmount).append("元").toString();


        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
//        ExtendParams extendParams = new ExtendParams();
//        extendParams.setSysServiceProviderId("2088100200300400500");


        // 支付超时，定义为5分钟
        String timeoutExpress = "5m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>(1);

        GoodsDetail goods = GoodsDetail.newInstance(product.getProductId().toString(), product.getName(),
                product.getCashDeposit().multiply(new BigDecimal(100)).longValue(),1);
        goodsDetailList.add(goods);


        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        return alipayTradePreOrder(builder,order.getDepositId());
    }

    public void updateProductPayment(long productOrderId,long productId){
        BigDecimal payment = productOrderMapper.selectPaymentByOrderId(productOrderId);
        Product product = new Product();
        product.setCurrentPrice(payment);
        product.setProductId(productId);
        int num = productMapper.updateByPrimaryKeySelective(product);
        if(num<=0)
            logger.error(productId+"拍品更新价格失败");
    }
    public ServerResponse depositRefund(Long orderId, Long userId){
        DepositOrder depositOrder = depositOrderMapper.selectByUserIdAndDepositId(userId,orderId);
        if(depositOrder.getStatus()!=OrderConst.DepositOrderStatusConst.CAN_REFUMD)
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.ILLEGAL_OPERATION);

        return depositRefund(depositOrder);
    }

    public ServerResponse depositRefund(DepositOrder depositOrder){

        // (必填) 外部订单号，需要退款交易的商户外部订单号
        String outTradeNo = String.valueOf(depositOrder.getDepositId());

        // (必填) 退款金额，该金额必须小于等于订单的支付金额，单位为元
        String refundAmount = String.valueOf(depositOrder.getPayment());

        // (可选，需要支持重复退货时必填) 商户退款请求号，相同支付宝交易号下的不同退款请求号对应同一笔交易的不同退款申请，
        // 对于相同支付宝交易号下多笔相同商户退款请求号的退款交易，支付宝只会进行一次退款
        //String outRequestNo = "";

        // (必填) 退款原因，可以说明用户退款原因，方便为商家后台提供统计
        String refundReason = "定金退款";

        // (必填) 商户门店编号，退款情况下可以为商家后台提供退款权限判定和统计等作用，详询支付宝技术支持
       // String storeId = "test_store_id";

        // 创建退款请求builder，设置请求参数

        //设置产品订单为取消状态

        int num = productOrderMapper.updateStatusByDepositIdAndStatus(OrderConst.ProductOrderStatusConst.CANCELED,OrderConst.ProductOrderStatusConst.WAIT_TO_REBID,depositOrder.getDepositId());
        if(num<=0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByError(OrderConst.OrderResponStateConst.REFUND_FAILED);
        }
        AlipayTradeRefundRequestBuilder builder = new AlipayTradeRefundRequestBuilder()
                .setOutTradeNo(outTradeNo).setRefundAmount(refundAmount).setRefundReason(refundReason);

        AlipayF2FRefundResult result = tradeService.tradeRefund(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                num = depositOrderMapper.updateStatusByDepositIdAndStatus(OrderConst.DepositOrderStatusConst.ORDER_CLOSED
                        , OrderConst.DepositOrderStatusConst.CAN_REFUMD,depositOrder.getDepositId());
                if(num<=0){
                    logger.info(depositOrder.getDepositId()+"退款成功但状态更新失败");
                    TransactionAspectSupport.currentTransactionStatus().flush();
                    throw new DatabaseUpdateErrorExecption(depositOrder.getDepositId()+"退款成功但状态更新失败");
                }
                int num1 = this.payInfoSwitchStatusByOrderId(depositOrder.getDepositId(),OrderConst.AlipayCallbackConst.TRADE_STATUS_RADE_CLOSED);

                if(num<=0||num1<=0){
                    logger.info(depositOrder.getDepositId()+"退款成功但状态更新失败");
                    TransactionAspectSupport.currentTransactionStatus().flush();
                    throw new DatabaseUpdateErrorExecption(depositOrder.getDepositId()+"退款成功但状态更新失败");
                }
                logger.info(depositOrder.getDepositId()+"支付宝退款成功: )");
                return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS);


            case FAILED:
                logger.error(depositOrder.getDepositId()+"支付宝退款失败!!!");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponse.createByError(OrderConst.OrderResponStateConst.REFUND_FAILED);

            case UNKNOWN:
                logger.error(depositOrder.getDepositId()+"系统异常，订单退款状态未知!!!");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new DatabaseUpdateErrorExecption(depositOrder.getDepositId()+"系统异常，订单退款状态未知!!!");

            default:
                logger.error(depositOrder.getDepositId()+"不支持的交易状态，交易返回异常!!!");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new DatabaseUpdateErrorExecption(depositOrder.getDepositId()+"不支持的交易状态，交易返回异常!!!");
        }
    }


    public ServerResponse orderCompleteDepositRefundToSeller(DepositOrder depositOrder){

        // (必填) 外部订单号，需要退款交易的商户外部订单号
        String outTradeNo = String.valueOf(depositOrder.getDepositId());

        // (必填) 退款金额，该金额必须小于等于订单的支付金额，单位为元
        String refundAmount = String.valueOf(depositOrder.getPayment());

        // (可选，需要支持重复退货时必填) 商户退款请求号，相同支付宝交易号下的不同退款请求号对应同一笔交易的不同退款申请，
        // 对于相同支付宝交易号下多笔相同商户退款请求号的退款交易，支付宝只会进行一次退款
        //String outRequestNo = "";

        // (必填) 退款原因，可以说明用户退款原因，方便为商家后台提供统计
        String refundReason = "交易成功定金退还";

        // (必填) 商户门店编号，退款情况下可以为商家后台提供退款权限判定和统计等作用，详询支付宝技术支持
        // String storeId = "test_store_id";

        // 创建退款请求builder，设置请求参数

        //设置产品订单为取消状态

        AlipayTradeRefundRequestBuilder builder = new AlipayTradeRefundRequestBuilder()
                .setOutTradeNo(outTradeNo).setRefundAmount(refundAmount).setRefundReason(refundReason);

        AlipayF2FRefundResult result = tradeService.tradeRefund(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                int num = depositOrderMapper.updateStatusByDepositIdAndStatus(OrderConst.DepositOrderStatusConst.ORDER_COMPLETED,depositOrder.getStatus(),depositOrder.getDepositId());

                if(num<=0){
                    logger.info(depositOrder.getDepositId()+"退款成功但状态更新失败");
                    TransactionAspectSupport.currentTransactionStatus().flush();
                    throw new DatabaseUpdateErrorExecption(depositOrder.getDepositId()+"退款成功但状态更新失败");
                }
                int num1 = this.payInfoSwitchStatusByOrderId(depositOrder.getDepositId(),OrderConst.AlipayCallbackConst.TRADE_STATUS_RADE_CLOSED);

                if(num<=0||num1<=0){
                    logger.info(depositOrder.getDepositId()+"退款成功但状态更新失败");
                    TransactionAspectSupport.currentTransactionStatus().flush();
                    throw new DatabaseUpdateErrorExecption(depositOrder.getDepositId()+"退款成功但状态更新失败");
                }
                logger.info(depositOrder.getDepositId()+"支付宝退款成功: )");
                return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS);


            case FAILED:
                logger.error(depositOrder.getDepositId()+"支付宝退款失败!!!");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponse.createByError(OrderConst.OrderResponStateConst.REFUND_FAILED);

            case UNKNOWN:
                logger.error(depositOrder.getDepositId()+"系统异常，订单退款状态未知!!!");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new DatabaseUpdateErrorExecption(depositOrder.getDepositId()+"系统异常，订单退款状态未知!!!");

            default:
                logger.error(depositOrder.getDepositId()+"不支持的交易状态，交易返回异常!!!");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new DatabaseUpdateErrorExecption(depositOrder.getDepositId()+"不支持的交易状态，交易返回异常!!!");
        }
    }
    public void changeOtherOrder(ProductOrder order){
        if (order==null)
            return ;
        int num = productOrderMapper.updateStatusAndDeadlineByOrderIdAndStatus(order.getOrderId(),
                OrderConst.ProductOrderStatusConst.WAIT_TO_REBID, OrderConst.ProductOrderStatusConst.BIDDING
                ,new Date(System.currentTimeMillis()+OrderConst.TimeoutConst.WAIT_REBID_TIME));
        num += depositOrderMapper.updateStatusByDepositIdAndStatus(OrderConst.DepositOrderStatusConst.CAN_REFUMD
                ,OrderConst.DepositOrderStatusConst.BIDDING,order.getDepositId());
        if (num!=2)
            logger.error(order.getOrderId()+"订单状态更新错误");
    }
    public ServerResponse refund(Long orderId,BigDecimal payment){
        // (必填) 外部订单号，需要退款交易的商户外部订单号
        String outTradeNo = String.valueOf(orderId);

        // (必填) 退款金额，该金额必须小于等于订单的支付金额，单位为元
        String refundAmount = String.valueOf(payment);

        // (可选，需要支持重复退货时必填) 商户退款请求号，相同支付宝交易号下的不同退款请求号对应同一笔交易的不同退款申请，
        // 对于相同支付宝交易号下多笔相同商户退款请求号的退款交易，支付宝只会进行一次退款
        //String outRequestNo = "";

        // (必填) 退款原因，可以说明用户退款原因，方便为商家后台提供统计
        String refundReason = "定金退款";

        // (必填) 商户门店编号，退款情况下可以为商家后台提供退款权限判定和统计等作用，详询支付宝技术支持
        // String storeId = "test_store_id";

        // 创建退款请求builder，设置请求参数

        //设置产品订单为取消状态
        AlipayTradeRefundRequestBuilder builder = new AlipayTradeRefundRequestBuilder()
                .setOutTradeNo(outTradeNo).setRefundAmount(refundAmount).setRefundReason(refundReason);

        AlipayF2FRefundResult result = tradeService.tradeRefund(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS);

            case FAILED:
                logger.error(orderId+"支付宝退款失败!!!");
                return ServerResponse.createByError(OrderConst.OrderResponStateConst.REFUND_FAILED);

            case UNKNOWN:
                logger.error(orderId+"系统异常，订单退款状态未知!!!");
                return ServerResponse.createByError(OrderConst.OrderResponStateConst.REFUND_FAILED);
            default:
                logger.error(orderId+"系统异常，订单退款状态未知!!!");
                return ServerResponse.createByError(OrderConst.OrderResponStateConst.REFUND_FAILED);
        }
    }

    //回调
    public ServerResponse aliCallback(Map<String,String> params){
        Long orderId = Long.parseLong(params.get("out_trade_no"));
        String tradeNo = params.get("trade_no");
        String tradeStatus = params.get("trade_status");
        String buyerLogonId = params.get("buyer_id");
        if(orderId%10==0){


            //获取定金单号
            DepositOrder order = depositOrderMapper.selectByPrimaryKey(orderId);
            if(order == null){
                return ServerResponse.createByError(OrderConst.OrderResponStateConst.DEPOSIT_ORDDER_INEXISTENCE);
            }
            if(order.getStatus() == OrderConst.DepositOrderStatusConst.BIDDING){
                logger.info("支付重复调用");
                return ServerResponse.createBySuccess();
            }

            //如果买家定金支付成功，更新订单
            if(OrderConst.AlipayCallbackConst.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)){
                //如果订单为买家定金订单
                if (order.getType()==1){
                    ProductOrder lastProductOrder = productOrderMapper.selectOrderByProductIdAndStatus(order.getProductId(), OrderConst.DepositOrderStatusConst.BIDDING);
                    if (lastProductOrder!=null){
                        changeOtherOrder(lastProductOrder);
                    }
                    updateProductPayment(order.getDepositId()+1,order.getProductId());
                }
                DepositOrder updateOne =new DepositOrder();
                updateOne.setGmtPayment(DateTimeUtil.strToDate(params.get("gmt_payment")));
                if (order.getType()==1)
                    updateOne.setStatus(OrderConst.DepositOrderStatusConst.BIDDING);
                else
                    updateOne.setStatus(OrderConst.DepositOrderStatusConst.WAIT_ORDER_COMPLETED);
                updateOne.setDepositId(order.getDepositId());
                int num = depositOrderMapper.updateByPrimaryKeySelective(updateOne);
                num+=productOrderMapper.updateStatusByDepositIdAndStatus(OrderConst.ProductOrderStatusConst.BIDDING, OrderConst.ProductOrderStatusConst.WAIT_TO_PAY_DEPOSIT, order.getDepositId());
                if (num<=0){
                    logger.info("定金订单更新失败");
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ServerResponse.createByError();
                }

                //如果订单为买家定金订单
                if (order.getType()==1){
                    int num1 = this.productOrderSwitchStatusByDepositOrderId(order.getDepositId(),OrderConst.ProductOrderStatusConst.BIDDING);
                    if (num1<=0){
                        logger.info("产品订单更新失败");
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return ServerResponse.createByError();
                     }
                }
            }

            PayInfo payInfo = new PayInfo();
            payInfo.setUserId(order.getPayerId());
            payInfo.setOrderId(order.getDepositId());
            payInfo.setPayPlatform(OrderConst.PayPlatformConst.ALIPAY);
            payInfo.setPlatformNumber(tradeNo);
            payInfo.setPlatformStatus(tradeStatus);
            payInfo.setType(OrderConst.OrderTypeConst.DEPOSIT_ORDER);
            payInfo.setBuyerLogonId(buyerLogonId);
            int num = payInfoMapper.insert(payInfo);
            if (num<=0){
                logger.info("定金支付信息更新失败");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponse.createByError();
            }
            //如果是卖家支付定金，更新商品状态
            if (order.getType()==OrderConst.DepositOrderTypeConst.SELLER){
                num = productMapper.updateStatusByPrimaryKeyAndUserId(order.getProductId(),order.getPayerId(),
                        ProductConst.ProductStatusConst.DEPOSIT_PAYED, ProductConst.ProductStatusConst.DRAFT_STATUS);
                if (num<=0)
                    logger.info("商品状态信息更新失败");
            }
            return ServerResponse.createBySuccess();

        }else{
            ProductOrder order = productOrderMapper.selectByPrimaryKey(orderId);

            if(order == null){
                return ServerResponse.createByError(OrderConst.OrderResponStateConst.PRODUCT_ORDDER_INEXISTENCE);
            }
            if(order.getStatus() >= OrderConst.ProductOrderStatusConst.WAIT_SELLER_DELIVER_GOODS){
                logger.info("支付重复调用");
                return ServerResponse.createBySuccess();
            }
            if(OrderConst.AlipayCallbackConst.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)){
                order.setGmtPayment(DateTimeUtil.strToDate(params.get("gmt_payment")));
                order.setStatus(OrderConst.ProductOrderStatusConst.WAIT_SELLER_DELIVER_GOODS);
                int num = productOrderMapper.updateByPrimaryKeySelective(order);
                if (num<=0){

                    logger.info(order.getOrderId()+"产品订单更新失败");
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ServerResponse.createByError();
                }
                num = this.depositOrderSwitchStatus(order.getDepositId(),OrderConst.DepositOrderStatusConst.WAIT_ORDER_COMPLETED);
                if (num<=0){

                    logger.info(order.getDepositId()+"定金订单状态更新失败");
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ServerResponse.createByError();
                }

            }

            PayInfo payInfo = new PayInfo();
            payInfo.setUserId(order.getBidderId());
            payInfo.setOrderId(order.getOrderId());
            payInfo.setBuyerLogonId(buyerLogonId);
            payInfo.setPayPlatform(OrderConst.PayPlatformConst.ALIPAY);
            payInfo.setType(OrderConst.OrderTypeConst.PRODUCT_ORDER);
            payInfo.setPlatformNumber(tradeNo);
            payInfo.setPlatformStatus(tradeStatus);
            int num = payInfoMapper.insert(payInfo);
            if (num<=0){
                logger.info("产品支付信息更新失败");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponse.createByError();
            }
            return ServerResponse.createBySuccess();
        }
    }

    public ServerResponse productOrderFianlPay(Long orderId, Long userId){


        ProductOrder order = productOrderMapper.selectByBidderIdAndOrderId(userId, orderId);
        System.out.println(order);
        if(order == null){
            return ServerResponse.createByError(OrderConst.OrderResponStateConst.PRODUCT_ORDDER_INEXISTENCE);
        }
        if(order.getStatus()!=OrderConst.ProductOrderStatusConst.WAIT_FINAL_PAY){
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.ILLEGAL_OPERATION);
        }
        Product product = productMapper.selectSimpleResultByPrimaryKey(order.getProductId());
        System.out.println(order.getProductId());
        if (product==null)
            return  ServerResponse.createByError(OrderConst.OrderResponStateConst.PRODUCT_INEXISTENCE);

        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderId().toString();


        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append(product.getName()).append("剩余金额支付,产品订单号:").append(outTradeNo).toString();


        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().subtract(product.getCashDeposit()).toString();


        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = totalAmount;



        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单").append(outTradeNo).append("拍卖商品待付金额").append(totalAmount).append("元").toString();


        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
//        ExtendParams extendParams = new ExtendParams();
//        extendParams.setSysServiceProviderId("2088100200300400500");


        // 支付超时，定义为5分钟
        String timeoutExpress = "5m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>(1);

        GoodsDetail goods = GoodsDetail.newInstance(product.getProductId().toString(), product.getName(),
                product.getCashDeposit().multiply(new BigDecimal(100)).longValue(),1);
        goodsDetailList.add(goods);


        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        ServerResponse serverResponse = alipayTradePreOrder(builder,order.getDepositId());

        return serverResponse;
    }

    public ServerResponse  deliverGoods(String logisticsCompany,String logisticsNumber,Long orderId,Long userId){
        Long productId = productOrderMapper.selectProductIdByPrimaryKey(orderId);
        if (productId==null)
            return ServerResponse.createByError(OrderConst.OrderResponStateConst.PRODUCT_ORDDER_INEXISTENCE);
        Long userId0 = productMapper.selectSellerIdByPrimaryKey(productId);
        if (userId0!=userId)
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.ILLEGAL_PARAMETETS);
        Invoice invoice = new Invoice();
        invoice.setProductOrderId(orderId);
        invoice.setLogisticsCompany(logisticsCompany);
        invoice.setLogisticsNumber(logisticsNumber);
        int num = invoiceMapper.updateByOrderIdSelective(invoice);
        if (num<=0){
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
        }
        num = productOrderMapper.updateStatusByOrderIdAndStatus(orderId,OrderConst.ProductOrderStatusConst.WAIT_BIDDER_RECEIVER
        ,OrderConst.ProductOrderStatusConst.WAIT_SELLER_DELIVER_GOODS);
        if (num<=0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
        }
        return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS);
    }

    public ServerResponse comfirmReceipt(Long orderId,Long bidderId){

        ProductOrder productOrder = productOrderMapper.selectByBidderIdAndOrderId(bidderId,orderId);
        if (productOrder==null)
            return ServerResponse.createByError(OrderConst.OrderResponStateConst.PRODUCT_ORDDER_INEXISTENCE);
        if (productOrder.getStatus()!= OrderConst.ProductOrderStatusConst.WAIT_BIDDER_RECEIVER)
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.ILLEGAL_OPERATION);
        int num = productOrderMapper.updateStatusByOrderIdAndStatus(orderId, OrderConst.ProductOrderStatusConst.WAIT_PAY_TO_SELLER
                ,OrderConst.ProductOrderStatusConst.WAIT_BIDDER_RECEIVER);
        if (num<=0)
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
        return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS);
    }

    @Transactional
    public ServerResponse sellerDeliverGoods(Long productId ,String logisticsNumber,String logisticsCompany,Long userId) {

        Long sellerId = productMapper.selectSellerIdByPrimaryKey(productId);
        if (sellerId != userId)
            return ServerResponse.createByErrorIllegalParam();
        Long orderId = productOrderMapper.selectOrderIdByProductIdAndStatus(productId, OrderConst.ProductOrderStatusConst.WAIT_SELLER_DELIVER_GOODS);
        if (orderId == null)
            return ServerResponse.createByError(OrderConst.OrderResponStateConst.PRODUCT_ORDDER_INEXISTENCE);
        Invoice invoice = new Invoice();
        invoice.setLogisticsNumber(logisticsNumber);
        invoice.setLogisticsCompany(logisticsCompany);
        invoice.setProductOrderId(orderId);
        int num = invoiceMapper.updateByOrderIdSelective(invoice);
        ProductOrder productOrder = new ProductOrder();
        productOrder.setStatus(OrderConst.ProductOrderStatusConst.WAIT_BIDDER_RECEIVER)
                .setGmtDeadline(new Date(System.currentTimeMillis()+OrderConst.TimeoutConst.WAIT_BIDDER_RECEIPT_TIME))
                .setOrderId(orderId);
        num += productOrderMapper.updateByPrimaryKeySelective(productOrder);
        if (num!=2){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponse.createByErrorUnknow();
        }
        return ServerResponse.createBySuccessOperationSuccess();
    }


//    public ServerResponse getUsersOrder(Long userId,int pageNum,int pageSize){
//        List<ProductOrder> list =productOrderMapper.selectOrderIdByStatus();
//        return null;
//    }



    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            logger.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                logger.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            logger.info("body:" + response.getBody());
        }
    }
    private ServerResponse alipayTradePreOrder(AlipayTradePrecreateRequestBuilder builder,long depositId){
        Map<String ,String> resultMap = Maps.newHashMap();
        resultMap.put("orderNo",String.valueOf(depositId));
        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                logger.info("支付宝预下单成功:");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                File folder = new File(QR_PATH);
                if(!folder.exists()){
                    folder.setWritable(true);
                    folder.mkdirs();
                }

                // 需要修改为运行机器上的路径
                //细节细节细节
                String qrPath = String.format(QR_PATH+"/qr-%s.png",response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png",response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);

                File targetFile = new File(QR_PATH,qrFileName);
                try {
                    FTPUtil.uploadFile(new FileInputStream(targetFile),qrFileName,QR_PATH);
                } catch (IOException e) {
                    logger.error("上传二维码异常",e);
                }
                logger.info("qrPath:" + qrPath);
                String qrUrl = PropertiesUtil.getProperty("ftp.server.http.prefix")+qrPath;
                resultMap.put("qrUrl",qrUrl);
                return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS,resultMap);
            case FAILED:
                logger.error("支付宝预下单失败!!!");
                return ServerResponse.createByError(OrderConst.OrderResponStateConst.PRE_ORDER_FAILED);

            case UNKNOWN:
                logger.error("系统异常，预下单状态未知!!!");
                return ServerResponse.createByError(OrderConst.OrderResponStateConst.PRE_ORDER_UNKNOWN);

            default:
                logger.error("不支持的交易状态，交易返回异常!!!");
                return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
        }
    }


    private int depositOrderSwitchStatus(long depositOrderId,byte status){
        DepositOrder udo = new DepositOrder();
        udo.setStatus(status);
        udo.setDepositId(depositOrderId);
        return depositOrderMapper.updateByPrimaryKeySelective(udo);
    }

    private int productOrderSwitchStatusByDepositOrderId(long depositOrderId,byte status){
        ProductOrder upo = new ProductOrder();
        upo.setStatus(status);
        upo.setDepositId(depositOrderId);
        return productOrderMapper.updateByDepositIdSelective(upo);
    }

    private int productOrderSwitchStatusByProductOrderId(long ProductOrderId,byte status){
        ProductOrder upo = new ProductOrder();
        upo.setStatus(status);
        upo.setOrderId(ProductOrderId);
        return productOrderMapper.updateByPrimaryKeySelective(upo);
    }


    private int payInfoSwitchStatusByOrderId(long orderId,String status){
        PayInfo payInfo = new PayInfo();
        payInfo.setOrderId(orderId);
        payInfo.setPlatformStatus(status);
        return payInfoMapper.updateByOrderIdSelective(payInfo);
    }

    public ServerResponse getPaymentStatus(Long orderId,Long userId){
        Byte status = null;
        if(orderId%10==0){
            status = depositOrderMapper.getStatusByPrimaryKeyAndUserId(orderId,userId);
        }else
            status = productOrderMapper.getStatusByPrimaryKeyAndUserId(orderId,userId);
        if (status==null)
            return ServerResponse.createByErrorUnknow();
        return ServerResponse.createBySuccessOperationSuccess(status);

    }
}
