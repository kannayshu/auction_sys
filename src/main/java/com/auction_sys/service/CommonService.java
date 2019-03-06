package com.auction_sys.service;

import com.auction_sys.common.ServerResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

/**
 * Created By liuda on 2018/5/24
 */

public interface CommonService {
    ServerResponse createCaptcha(HttpSession httpSession);
    ServerResponse verifyCaptchaPeek(HttpSession httpSession,String captcha);
    ServerResponse verifyCaptcha(HttpSession httpSession,String captcha);
}
