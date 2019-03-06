package com.auction_sys.common.constant;

/**
 * Created By liuda on 2018/7/11
 */
public class RedisConst {
    public interface  REDIS_LOCK{
        String CLOSE_ORDER_TASK_LOCK = "CLOSE_ORDER_TASK_LOCK";//关闭订单的分布式锁
    }
    public interface RedisCacheExtime{
        int REDIS_SESSION_EXTIME = 60 * 30;//30分钟
    }

}
