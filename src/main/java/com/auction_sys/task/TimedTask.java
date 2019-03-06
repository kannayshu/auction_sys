package com.auction_sys.task;

import com.alipay.AlipayClientHolder;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.auction_sys.common.constant.OrderConst;
import com.auction_sys.common.constant.ProductConst;
import com.auction_sys.dao.*;
import com.auction_sys.pojo.*;
import com.auction_sys.service.OrderService;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.javassist.runtime.Inner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * Created By liuda on 2018/7/12
 */

public class TimedTask {

    private final static Logger logger = LoggerFactory.getLogger(TimedTask.class);

    @Autowired
    ProductMapper productMapper;
    @Autowired
    ProductOrderMapper productOrderMapper;
    @Autowired
    DepositOrderMapper depositOrderMapper;
    @Autowired
    TimedTask timedTask;
    @Autowired
    OrderService orderService;
    @Autowired
    PayInfoMapper payInfoMapper;
    @Autowired
    InnerOrderMapper innerOrderMapper;

    private final static double RATE_OF_COMMISSION = 0;
    AlipayClient alipayClient = AlipayClientHolder.getInstance();


    //当拍品到达deadline时间后将相应订单变成等待最后付款状态
    @Scheduled(cron ="0 */1 * * * ?" )
    public void finishBidOrder(){
        List<Long>  productIdList = productMapper.selectDeadlineIdByStatus(ProductConst.ProductStatusConst.BIDDING);
        for (long id:productIdList) {
            timedTask.finishBidOrderSub(id);
        }
    }
    @Transactional
    public void finishBidOrderSub(long id){
        Object savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        try{
            //todo 查询逻辑有冗余待重写
            int num = productMapper.updateStatusByPrimaryKey(ProductConst.ProductStatusConst.TIME_OVER,id);
            Long poId = productOrderMapper.selectOrderIdByProductIdAndStatus(id,OrderConst.ProductOrderStatusConst.BIDDING);
            //如果没有拍卖订单，退还卖家定金
            if(poId==null){
                Long sellerId = productMapper.selectSellerIdByPrimaryKey(id);
                DepositOrder depositOrder = depositOrderMapper.selectDepositByPayerIdAndTypeAndProductId(sellerId,
                        OrderConst.DepositOrderTypeConst.SELLER,id);
                orderService.orderCompleteDepositRefundToSeller(depositOrder);
            }
            num += productOrderMapper.updateStatusAndDeadlineByOrderIdAndStatus(poId,OrderConst.ProductOrderStatusConst.WAIT_FINAL_PAY, OrderConst.ProductOrderStatusConst.BIDDING
            ,new Date(System.currentTimeMillis()+ OrderConst.TimeoutConst.WAIT_BIDDER_FINAL_PAY_TIME));
            long doId = depositOrderMapper.selectDepositIdByProductIdAndStatusAndType(id,OrderConst.DepositOrderStatusConst.WAIT_ORDER_COMPLETED
                    ,OrderConst.DepositOrderTypeConst.BIDDER);
            num+= depositOrderMapper.updateStatusByDepositIdAndStatus(OrderConst.DepositOrderStatusConst.WAIT_ORDER_COMPLETED,OrderConst.DepositOrderStatusConst.BIDDING,doId);
            if(num<3) {
                logger.info("拍品ID"+id+"定时关单失败");
                TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            }
        }catch (Exception e){
            logger.error("拍品ID"+id+"定时关单失败",e);
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
        }
    }


    //当支付定金订单时间超时进行关单
    @Scheduled(cron ="0 */1 * * * ?" )
    public void closeDepositOrder(){
        List<Long>  doIdList = depositOrderMapper.selectDepositIdByGmtdeadlineAndStatus(OrderConst.DepositOrderStatusConst.WAIT_TO_PAY);
        for (long id:doIdList) {
           timedTask.closeDepositOrderSub(id);
        }
    }

    @Transactional
    public void closeDepositOrderSub(long id){
        Object savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        try{
            int num =depositOrderMapper.updateStatusByDepositIdAndStatus(OrderConst.DepositOrderStatusConst.ORDER_CLOSED,
                    OrderConst.DepositOrderStatusConst.WAIT_TO_PAY,id);
            num+=productOrderMapper.updateStatusByDepositIdAndStatus(OrderConst.ProductOrderStatusConst.CANCELED,
                    OrderConst.ProductOrderStatusConst.WAIT_TO_PAY_DEPOSIT,id);
            if(num<2) {
                logger.error("定金订单ID"+id+"定时关单失败");
                TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            }
        }catch (Exception e){
            logger.error("定金订单ID"+id+"定时关单失败",e);
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
        }
    }

    //当支付最后金额订单时间超时进行关单
    @Scheduled(cron ="0 */1 * * * ?" )
    public void closeProductOrder(){
        List<Long>  doIdList = productOrderMapper.selectOrderIdByGmtDeadlineAndStatus(OrderConst.ProductOrderStatusConst.WAIT_FINAL_PAY);
        for (long id:doIdList) {
            timedTask.closeProductOrderSub(id);
        }
    }

    @Transactional
    public void closeProductOrderSub(long id){
        Object savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        try{
            int num =productOrderMapper.updateStatusByOrderIdAndStatus(id,OrderConst.ProductOrderStatusConst.BIDDER_BREAK_CONTACT
                    , OrderConst.ProductOrderStatusConst.WAIT_FINAL_PAY);
            //todo 买家违约后需要做的操作
            if(num<1) {
                logger.error("定金订单ID"+id+"定时关单失败");
                TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
                return;
            }
        }catch (Exception e){
            logger.error("定金订单ID"+id+"定时关单失败",e);
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
        }
    }

    //对重新出价超时的订单进行定金退款
    @Scheduled(cron ="0 */1 * * * ?" )
    public void closeRefunddingProductOrder(){
        List<Long>  doIdList = productOrderMapper.selectOrderIdByGmtDeadlineAndStatus(OrderConst.ProductOrderStatusConst.WAIT_TO_REBID);

        for (long id:doIdList) {
            logger.info("订单"+id+"重新出价超时");
            long doId = productOrderMapper.selectDepositIdByPrimaryKey(id);
            DepositOrder depositOrder = depositOrderMapper.selectByPrimaryKey(doId);
            orderService.depositRefund(depositOrder);
        }
    }


    @Scheduled(cron ="0 */5 * * * ?" )
    public void transferToSeller(){
        List<Long>  poIdList = productOrderMapper.selectOrderIdByStatus(OrderConst.ProductOrderStatusConst.WAIT_PAY_TO_SELLER);
        for (long id:poIdList) {
            timedTask.transferToSellerSub(id);
        }
    }


    @Transactional
    public void transferToSellerSub(long id){
        Object savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();

        //获取买家总共支付金额，减去中间费用转给卖家
        ProductOrder productOrder = productOrderMapper.selectByPrimaryKey(id);
        if (productOrder==null)
            return;
        if (productOrder.getStatus()!=OrderConst.ProductOrderStatusConst.WAIT_PAY_TO_SELLER){
            logger.error("订单ID"+productOrder.getOrderId()+"订单异常");
            return;
        }
        Product product = productMapper.selectSimpleResultByPrimaryKey(productOrder.getProductId());
        if (product==null)
            return;
        BigDecimal payment = productOrder.getPayment().multiply(new BigDecimal(1-RATE_OF_COMMISSION));
        //卖家定金订单Id
        Long sellerDepositId = depositOrderMapper.selectDepositIdByPayerIdAndTypeAndProductId(product.getSellerId(),
                OrderConst.DepositOrderTypeConst.SELLER,product.getProductId());
        if (sellerDepositId==null)
            return;

        //更新转账信息
        if (product.getStatus()!=ProductConst.ProductStatusConst.TIME_OVER)
            return;
        int num = productMapper.updateStatusByPrimaryKey(ProductConst.ProductStatusConst.COMPLETED,product.getProductId());
        if (num<=0)
            return;
        num += productOrderMapper.updateStatusByOrderIdAndStatus(productOrder.getOrderId(),
                OrderConst.ProductOrderStatusConst.ORDER_COMPLETED,OrderConst.ProductOrderStatusConst.WAIT_PAY_TO_SELLER);
        num += depositOrderMapper.updateStatusByDepositIdAndStatus(OrderConst.DepositOrderStatusConst.ORDER_COMPLETED,
                OrderConst.DepositOrderStatusConst.WAIT_ORDER_COMPLETED,productOrder.getDepositId());
        num += depositOrderMapper.updateStatusByDepositIdAndStatus(OrderConst.DepositOrderStatusConst.CAN_REFUMD, OrderConst.DepositOrderStatusConst.WAIT_ORDER_COMPLETED,sellerDepositId);
        if (num<4){
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            logger.error("更新订单错误");
            return;
        }

        String logonId = payInfoMapper.selectBuyerLogonIdByOrderId(sellerDepositId);
        if (logonId==null){
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            logger.error("更新订单错误");
            return;
        }
        //转账给卖家
        long currentTime = System.currentTimeMillis();
        Long innerId = currentTime*10+2;
        String remark = new StringBuilder().append("拍品").append(product.getName()).append("ID").append(product.getProductId())
                .append("交易成功结账").toString();
        String requestContent = transferContent(innerId.toString(),logonId,payment.toString(),remark);
        AlipayFundTransToaccountTransferRequest request =new AlipayFundTransToaccountTransferRequest();
        request.setBizContent(requestContent);
        AlipayFundTransToaccountTransferResponse response = null;
        try {
            response = alipayClient.execute(request);

        } catch (AlipayApiException e) {
            if (!response.isSuccess())
                TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            logger.error(e.getErrMsg(),e);
            return;
        }
        if (!response.isSuccess()){

            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            logger.error("结算转账失败");
        }
        //转账成功马上将已经更新的信息提交，以防后面的逻辑发生错误导致回滚但已经支付
        TransactionAspectSupport.currentTransactionStatus().flush();
        InnerOrder innerOrder = new InnerOrder();
        innerOrder.setInnerOrderId(innerId).setPayment(payment).setProductOrderId(productOrder.getOrderId()).setRemark(remark);
        num = innerOrderMapper.insert(innerOrder);
        if (num<=0){
            logger.error("内部订单生成失败");
        }
        //退还卖家保证金
        orderService.orderCompleteDepositRefundToSeller(depositOrderMapper.selectByPrimaryKey(sellerDepositId));
    }


    private String transferContent(String outBizNo,String logonId,String amount,String remark){
        String s = "{" +
                "\"out_biz_no\":\""+outBizNo+"\"," +
                "\"payee_type\":\"ALIPAY_USERID\"," +
                "\"payee_account\":\""+logonId+"\"," +
                "\"amount\":\""+amount+"\"," +
                "\"remark\":\""+remark+"\"" +
                "}";
        return s;
    }



    @Scheduled(cron ="0 */5 * * * ?" )
    public void disposeSellerBreakContact(){

        List<Long>  poIdList = productOrderMapper.selectOrderIdByStatus(OrderConst.ProductOrderStatusConst.SELLER_BREAK_CONTACT);
        for (long id:poIdList) {
            timedTask.disposeSellerBreakContactSub(id);
        }
    }

    @Transactional
    public void disposeSellerBreakContactSub(Long id){
        Object savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        //获取卖家定金金额，将定金转给买家
        ProductOrder productOrder = productOrderMapper.selectByPrimaryKey(id);
        if (productOrder==null)
            return;
        if (productOrder.getStatus()!=OrderConst.ProductOrderStatusConst.SELLER_BREAK_CONTACT){
            logger.error("订单ID"+productOrder.getOrderId()+"订单异常");
            return;
        }
        Product product = productMapper.selectSimpleResultByPrimaryKey(productOrder.getProductId());
        if (product==null)
            return;
        if (product.getStatus()!=ProductConst.ProductStatusConst.TIME_OVER)
            return;
        //获取买家定金订单
        DepositOrder bidderDepositOrder = depositOrderMapper.selectByPrimaryKey(productOrder.getDepositId());
        if (bidderDepositOrder==null)
            return;
        //获取卖家定金订单
        DepositOrder sellerDepositOrder = depositOrderMapper.selectDepositByPayerIdAndTypeAndProductId(product.getSellerId(),
                OrderConst.DepositOrderTypeConst.SELLER,product.getProductId());
        if (sellerDepositOrder==null|sellerDepositOrder.getStatus()!= OrderConst.DepositOrderStatusConst.WAIT_ORDER_COMPLETED)
            return;
        //将商品设置为完成状态
        int num = productMapper.updateStatusByPrimaryKey(ProductConst.ProductStatusConst.COMPLETED,product.getProductId());
        if (num<=0)
            return;

        //将商品订单设置为关闭状态
        num += productOrderMapper.updateStatusByOrderIdAndStatus(productOrder.getOrderId(),
                OrderConst.ProductOrderStatusConst.ORDER_CLOSED,OrderConst.ProductOrderStatusConst.SELLER_BREAK_CONTACT);
        //将买家定金订单设置为关闭状态
        num += depositOrderMapper.updateStatusByDepositIdAndStatus(OrderConst.DepositOrderStatusConst.ORDER_CLOSED,
                OrderConst.DepositOrderStatusConst.WAIT_ORDER_COMPLETED,productOrder.getDepositId());
        //将卖家定金订单设置为关闭状态
        num += depositOrderMapper.updateStatusByDepositIdAndStatus(OrderConst.DepositOrderStatusConst.ORDER_CLOSED, OrderConst.DepositOrderStatusConst.BIDDING,
                sellerDepositOrder.getDepositId());
        if (num<4){
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            logger.error("更新订单错误");
            return;
        }

        //获取买家支付宝账号
        String bidderLogonId = payInfoMapper.selectBuyerLogonIdByOrderId(bidderDepositOrder.getDepositId());
        if (bidderLogonId==null){
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            logger.error("更新订单错误");
            return;
        }
        //将卖家定金转账给买家
        long currentTime = System.currentTimeMillis();
        Long innerId = currentTime*10+2;
        String remark = new StringBuilder().append("拍品").append(product.getName()).append("ID").append(product.getProductId())
                .append("卖家违约定金").toString();
        String requestContent = transferContent(innerId.toString(),bidderLogonId,sellerDepositOrder.getPayment().toString(),remark);
        AlipayFundTransToaccountTransferRequest request =new AlipayFundTransToaccountTransferRequest();
        request.setBizContent(requestContent);
        AlipayFundTransToaccountTransferResponse response = null;
        try {
            response = alipayClient.execute(request);

        } catch (AlipayApiException e) {
            if (!response.isSuccess())
                TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            logger.error(e.getErrMsg(),e);
            return;
        }
        if (!response.isSuccess()){

            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            logger.error("结算转账失败");
        }
        TransactionAspectSupport.currentTransactionStatus().flush();
        InnerOrder innerOrder = new InnerOrder();
        innerOrder.setInnerOrderId(innerId).setPayment(sellerDepositOrder.getPayment()).setProductOrderId(productOrder.getOrderId()).setRemark(remark);
        num = innerOrderMapper.insert(innerOrder);
        if (num<=0){
            logger.error("内部订单生成失败");
        }
        //退还买家保证金
        orderService.depositRefund(bidderDepositOrder);
    }


    @Scheduled(cron ="0 */5 * * * ?" )
    public void disposeBidderBreakContact(){

        List<Long>  poIdList = productOrderMapper.selectOrderIdByStatus(OrderConst.ProductOrderStatusConst.BIDDER_BREAK_CONTACT);
        for (long id:poIdList) {
            timedTask.disposeBidderBreakContactSub(id);
        }
    }

    @Transactional
    public void disposeBidderBreakContactSub(Long id){
        Object savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        //获取买家定金金额，将定金转给卖家
        ProductOrder productOrder = productOrderMapper.selectByPrimaryKey(id);
        if (productOrder==null||productOrder.getStatus()!=OrderConst.ProductOrderStatusConst.BIDDER_BREAK_CONTACT){
            logger.error("订单ID"+productOrder.getOrderId()+"订单异常");
            return;
        }
        Product product = productMapper.selectSimpleResultByPrimaryKey(productOrder.getProductId());
        if (product==null)
            return;
        if (product.getStatus()!=ProductConst.ProductStatusConst.TIME_OVER)
            return;
        //获取买家定金订单
        DepositOrder bidderDepositOrder = depositOrderMapper.selectByPrimaryKey(productOrder.getDepositId());
        if (bidderDepositOrder==null)
            return;
        //获取卖家定金订单
        DepositOrder sellerDepositOrder = depositOrderMapper.selectDepositByPayerIdAndTypeAndProductId(product.getSellerId(),
                OrderConst.DepositOrderTypeConst.SELLER,product.getProductId());
        if (sellerDepositOrder==null|sellerDepositOrder.getStatus()!= OrderConst.DepositOrderStatusConst.WAIT_ORDER_COMPLETED)
            return;
        //将商品设置为完成状态
        int num = productMapper.updateStatusByPrimaryKey(ProductConst.ProductStatusConst.COMPLETED,product.getProductId());
        if (num<=0)
            return;

        //将商品订单设置为关闭状态
        num += productOrderMapper.updateStatusByOrderIdAndStatus(productOrder.getOrderId(),
                OrderConst.ProductOrderStatusConst.ORDER_CLOSED,OrderConst.ProductOrderStatusConst.BIDDER_BREAK_CONTACT);
        //将买家定金订单设置为关闭状态
        num += depositOrderMapper.updateStatusByDepositIdAndStatus(OrderConst.DepositOrderStatusConst.ORDER_CLOSED,
                OrderConst.DepositOrderStatusConst.WAIT_ORDER_COMPLETED,productOrder.getDepositId());
        //将卖家定金订单设置为可退款状态
        num += depositOrderMapper.updateStatusByDepositIdAndStatus(OrderConst.DepositOrderStatusConst.CAN_REFUMD, OrderConst.DepositOrderStatusConst.WAIT_ORDER_COMPLETED,
                sellerDepositOrder.getDepositId());
        //将卖家商品信息设置为定金已付状态
        num += productMapper.updateStatusByPrimaryKey(ProductConst.ProductStatusConst.DEPOSIT_PAYED,product.getProductId());
        if (num<5){
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            logger.error("更新订单错误");
            return;
        }

        //获取卖家支付宝账号
        String sellerLogonId = payInfoMapper.selectBuyerLogonIdByOrderId(sellerDepositOrder.getDepositId());
        if (sellerLogonId==null){
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            logger.error("更新订单错误");
            return;
        }
        //将买家定金转账给卖家
        long currentTime = System.currentTimeMillis();
        Long innerId = currentTime*10+2;
        String remark = new StringBuilder().append("拍品").append(product.getName()).append("ID").append(product.getProductId())
                .append("买家违约定金").toString();
        String requestContent = transferContent(innerId.toString(),sellerLogonId,bidderDepositOrder.getPayment().toString(),remark);
        AlipayFundTransToaccountTransferRequest request =new AlipayFundTransToaccountTransferRequest();
        request.setBizContent(requestContent);
        AlipayFundTransToaccountTransferResponse response = null;
        try {
            response = alipayClient.execute(request);
            dumpResponse(response);
        } catch (AlipayApiException e) {
            if (!response.isSuccess())
                TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            logger.error(e.getErrMsg(),e);
            return;
        }
        if (!response.isSuccess()){

            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            logger.error("结算转账失败");
        }
        InnerOrder innerOrder = new InnerOrder();
        innerOrder.setInnerOrderId(innerId).setPayment(sellerDepositOrder.getPayment()).setProductOrderId(productOrder.getOrderId()).setRemark(remark);
        num = innerOrderMapper.insert(innerOrder);
        if (num<=0){
            logger.error("内部订单生成失败");
        }
        //删除所有与该商品相关订单

        deleteOrderByProductId(product.getProductId());
    }


    public void deleteOrderByProductId(Long productId){
        productOrderMapper.deleteByProductId(productId);
        depositOrderMapper.deleteByProductIdAndType(productId, OrderConst.DepositOrderTypeConst.BIDDER);
    }



    public void closeRebidTimeoutOrder(){
        List<Long> ids = productOrderMapper.selectOrderIdByGmtDeadlineAndStatus(OrderConst.ProductOrderStatusConst.WAIT_TO_REBID);
        for (Long id:ids) {
            DepositOrder depositOrder = depositOrderMapper.selectByPrimaryKey(id-1);
            orderService.depositRefund(depositOrder);
        }
    }
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

    @Transactional
    public void transTo(){

        long currentTime = System.currentTimeMillis();
        Long innerId = currentTime*10+2;
        String remark = new StringBuilder().append("拍品").append("123456Apple iPhone 7 Plus (A1661) 128G 玫瑰金色 移动联通电信4G手机").append("ID").append("34")
                .append("买家违约定金").toString();
        String requestContent = transferContent(innerId.toString(),"2088102176457272",new BigDecimal(15).toString(),remark);
        AlipayFundTransToaccountTransferRequest request =new AlipayFundTransToaccountTransferRequest();
        request.setBizContent(requestContent);
        AlipayFundTransToaccountTransferResponse response = null;
        try {
            response = alipayClient.execute(request);
            dumpResponse(response);
        } catch (AlipayApiException e) {
            if (!response.isSuccess())
                logger.error(e.getErrMsg(), e);
            return;
        }
        throw new RuntimeException();

    }
}
