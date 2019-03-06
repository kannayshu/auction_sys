package controller;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.controller.CommonController;
import com.auction_sys.controller.LoginedUserController;
import com.auction_sys.controller.UserController;
import com.auction_sys.pojo.User;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import test.TestBase;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created By liuda on 2018/5/27
 */
@Slf4j
public class UserControllerTest extends TestBase{

    private final static Logger log = LoggerFactory.getLogger(UserControllerTest.class);
    @Autowired
    CommonController commonController;
    @Autowired
    UserController userController;
    @Autowired
    LoginedUserController loginedUserController;
    ObjectMapper objectMapper = new ObjectMapper();

    MockHttpServletRequest request = new MockHttpServletRequest();

    MockHttpSession session = new MockHttpSession();

    MockHttpServletResponse response = new MockHttpServletResponse();
    @Test
    public void testRegister() throws InterruptedException {
        ServerResponse response = commonController.createCaptcha(session);
        log.info("code:"+response.getMsg());
        String captcha = (String) session.getAttribute("captcha");
        User u = new User();
        u.setUsername("liudaxx");
        u.setEmail("liudaxx@126.com");
        u.setPassword("liudaxxx");
        response = userController.register(u,captcha,session);
        log.info("code:"+response.getMsg());
        response = commonController.createCaptcha(session);
        log.info("code:"+response.getMsg());
        captcha = (String) session.getAttribute("captcha");
        u.setPassword("liudaxxx");
        response = userController.login(u,captcha,session);
        log.info("code:"+response.getMsg());
        System.out.println(session.getId());
        response = loginedUserController.activeEmail(session);
        log.info("code:"+response.getMsg());
        Thread.sleep(10000000);

    }
    @Test
    public void testActice(){
        User u = new User();
        u.setUsername("liudaxx");
        u.setEmail("liudaxx@126.com");
        u.setPassword("liudaxxx");

    }


    @Test
    public void forgetPassword() throws IOException {

        JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
                .createJsonGenerator(System.out, JsonEncoding.UTF8);

        ServerResponse serverResponse =  userController.forgetPassword("liudaxx@126.com");
        String serJsonStr = objectMapper.writeValueAsString(serverResponse);
        log.info(serJsonStr);
    }

    @Test
    public void retrievePassword() throws IOException {

        JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
                .createJsonGenerator(System.out, JsonEncoding.UTF8);

        ServerResponse serverResponse =  userController.retrievePassword("liudaxx@126.com","88888888","4999fe6d-248f-4757-afa9-d88b7b6b76f4");
        String serJsonStr = objectMapper.writeValueAsString(serverResponse);
        log.info(serJsonStr);
    }


}
