package test;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.controller.UserController;
import com.auction_sys.pojo.User;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager")
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public abstract class TestBase {
//public abstract class TestBase extends AbstractTransactionalJUnit4SpringContextTests {
MockHttpServletRequest request = new MockHttpServletRequest();

    @Autowired
    protected UserController userController;
    protected MockHttpSession session = new MockHttpSession();

    protected MockHttpServletResponse response = new MockHttpServletResponse();
    protected ObjectMapper objectMapper = new ObjectMapper();

    protected void printServerRespon(ServerResponse serverResponse) throws IOException {
        JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
                .createJsonGenerator(System.out, JsonEncoding.UTF8);
        String serJsonStr = objectMapper.writeValueAsString(serverResponse);
        System.out.println(serJsonStr);
    }

    protected void login() throws IOException {
        User user = new User();
        user.setUsername("liudaxx");
        user.setPassword("88888888");
        ServerResponse serverResponse = userController.loginWithoutChaptcha(user,session);
    }

}
