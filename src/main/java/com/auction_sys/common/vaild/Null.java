package com.auction_sys.common.vaild;

import java.lang.annotation.*;

/**
 * Created By liuda on 2018/7/18
 */
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
public @interface Null {
    byte value() default VaildType.INSERT;
}
