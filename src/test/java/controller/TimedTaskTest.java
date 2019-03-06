package controller;

import com.auction_sys.task.TimedTask;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.TestBase;

/**
 * Created By liuda on 2018/7/12
 */
public class TimedTaskTest extends TestBase {

    @Autowired
    TimedTask timedTask;
    @Test
    public void testFinishOrder(){

        timedTask.finishBidOrder();
    }

    @Test
    public void testCloseDepositOrder(){

        timedTask.closeDepositOrder();
    }

    @Test
    public void testCloseProductOrder(){
        timedTask.closeProductOrder();
    }
    @Test
    public void testCloseRefunddingProductOrder(){
        timedTask.closeRefunddingProductOrder();
    }

    @Test
    public void testFinishBidOrder(){
        timedTask.finishBidOrder();
    }

    @Test
    public void testTransfer(){
        timedTask.transferToSeller();
    }

    @Test
    public void testDisposeBidderBreakContact(){
        timedTask.disposeBidderBreakContact();
    }

    @Test
    public void transTo(){
        timedTask.transTo();
    }

}


