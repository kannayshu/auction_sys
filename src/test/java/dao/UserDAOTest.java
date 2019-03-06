package dao;

import com.auction_sys.dao.UserMapper;
import com.auction_sys.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.TestBase;

/**
 * Created By liuda on 2018/5/24
 */
@Slf4j
public class UserDAOTest extends TestBase {
    @Autowired
    UserMapper userMapper;
    @Test
    public void testInsert(){
        User u = new User(null,(byte)1,"sssdss","1","ssasss",(byte)1);
        int i = userMapper.insert(u);
        Exception e = new Exception("info");
        System.out.println(u.getUserId());
        log.info(e.getMessage(),e);
        System.out.println(i);
    }
}
