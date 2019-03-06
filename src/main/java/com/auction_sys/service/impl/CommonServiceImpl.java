package com.auction_sys.service.impl;


import com.auction_sys.common.ServerResponse;
import com.auction_sys.common.constant.CommonConst;
import com.auction_sys.service.CommonService;
import com.auction_sys.util.CaptchaUtil;
import com.auction_sys.util.FTPUtil;
import com.auction_sys.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created By liuda on 2018/5/24
 */
@Slf4j
@Service
@Transactional
public class CommonServiceImpl implements CommonService{

    private final static Logger logger = LoggerFactory.getLogger(CommonService.class);
    final static String ftphost = PropertiesUtil.getProperty("ftp.server.http.prefix","true");
    @Override
    public ServerResponse createCaptcha(HttpSession httpSession) {
        CaptchaUtil captchaUtil = CaptchaUtil.Instance();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String uuid;
        try {
            ImageIO.write(captchaUtil.getImage(),"JPEG",os);
            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
            uuid = UUID.randomUUID().toString();
            FTPUtil.uploadFile(is, uuid+".jpg","captcha");
        } catch (IOException e) {

            return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
        }
        httpSession.setAttribute("captcha_",captchaUtil.getString().toLowerCase());
        logger.info("sessionId{}",httpSession.getId());
        return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS,ftphost+"captcha/"+uuid+".jpg");
    }

    @Override
    public ServerResponse verifyCaptchaPeek(HttpSession httpSession,String captcha) {
        String string = (String) httpSession.getAttribute("captcha_");
        logger.info("sessionId{}",httpSession.getId());
        logger.info("captcha,{},captcha_,{}",captcha,string);
        if(StringUtils.isBlank(string))
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.CAPTCHA_FAULT);
        if(string.equals(captcha.toLowerCase()))
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.CAPTCHA_RIGHT);
        return ServerResponse.createByError(CommonConst.CommonResponStateConst.CAPTCHA_FAULT);
    }

    @Override
    public ServerResponse verifyCaptcha(HttpSession httpSession,String captcha) {
        String string = (String) httpSession.getAttribute("captcha_");

        logger.info("sessionId{}",httpSession.getId());
        logger.info("captcha,{},captcha_,{}",captcha,string);
        if(StringUtils.isBlank(string))
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.CAPTCHA_FAULT);
        httpSession.removeAttribute("captcha_");
        if(string.equals(captcha.toLowerCase())){
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.CAPTCHA_RIGHT);
        }
        return ServerResponse.createByError(CommonConst.CommonResponStateConst.CAPTCHA_FAULT);
    }

}
