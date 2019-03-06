package com.auction_sys.common.vaild;

import com.auction_sys.pojo.Product;
import com.auction_sys.pojo.ProductOrder;
import com.auction_sys.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;

/**
 * Created By liuda on 2018/7/17
 */
public class ParamVaild {
    private final static Logger logger = LoggerFactory.getLogger(ParamVaild.class);
    private ParamVaild(){
    }

    private static class ParamVaildHolder{
        private static final ParamVaild instantce = new ParamVaild();
    }
    private Annotation anno;

    private ParamVaild paramVaild;
    private static BigDecimal ZERO = new BigDecimal(0);

    public boolean insertable(Object obj) {
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object val = field.get(obj);
                Annotation[] annotations = field.getDeclaredAnnotations();
                inner:
                for (Annotation anno : annotations) {
                    if (anno.annotationType().equals(BigDecimalNonnegative.class)) {
                        BigDecimalNonnegative anno0 = (BigDecimalNonnegative) anno;
                        if ((anno0.value() & VaildType.INSERT) == VaildType.INSERT) {
                            if (val == null)
                                continue inner;
                            if (ZERO.compareTo((BigDecimal) val) < 0)
                                return false;
                            break inner;
                        }
                    }
                    if (anno.annotationType().equals(NotNull.class)) {
                        NotNull anno0 = (NotNull) anno;
                        if ((anno0.value() & VaildType.INSERT) == VaildType.INSERT) {
                            if (val == null)
                                return false;
                            break inner;
                        }
                    }
                    if (anno.annotationType().equals(Null.class)) {
                        Null anno0 = (Null) anno;
                        if ((anno0.value() & VaildType.INSERT) == VaildType.INSERT) {
                            if (val != null)
                                field.set(obj,null);
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return false;
        }
        return true;
    }


    public boolean updateable(Object obj) {
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object val = field.get(obj);
                Annotation[] annotations = field.getDeclaredAnnotations();
                inner:
                for (Annotation anno : annotations) {
                    if (anno.annotationType().equals(BigDecimalNonnegative.class)) {
                        BigDecimalNonnegative anno0 = (BigDecimalNonnegative) anno;
                        if ((anno0.value() & VaildType.UPDATE) == VaildType.UPDATE) {
                            if (val == null)
                                continue inner;
                            if (ZERO.compareTo((BigDecimal) val) < 0)
                                return false;
                            break inner;
                        }
                    }
                    if (anno.annotationType().equals(NotNull.class)) {
                        NotNull anno0 = (NotNull) anno;
                        if ((anno0.value() & VaildType.UPDATE) == VaildType.UPDATE) {
                            if (val == null)
                                return false;
                            break inner;
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return false;
        }
        return true;
    }


    public static final ParamVaild  getInstance(){
        return ParamVaildHolder.instantce;
    }
}
