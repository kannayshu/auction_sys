package test;

import com.auction_sys.common.vaild.ParamVaild;
import com.auction_sys.common.vaild.VaildType;
import com.auction_sys.pojo.Shipping;

import java.math.BigDecimal;

/**
 * Created By liuda on 2018/6/3
 */
public class Main {
    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException {
        Shipping shipping = new Shipping(null,1l,"S","s","S","s","S","s","S",null,null);
        boolean bool = ParamVaild.getInstance().insertable(shipping);
        System.out.println(bool);
        System.out.println(shipping.getUserId());
    }
}
