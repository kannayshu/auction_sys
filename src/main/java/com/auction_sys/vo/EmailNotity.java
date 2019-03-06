package com.auction_sys.vo;

import com.auction_sys.exception.EmailAddressIllegalExecption;
import com.auction_sys.service.impl.OrderServiceImpl;
import com.auction_sys.util.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created By liuda on 2018/5/27
 */

public class EmailNotity implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(EmailNotity.class);
    String content;
    String toEmail;
    public EmailNotity(String content, String toEmail){
        this.content = content;
        this.toEmail = toEmail;
    }
    public void execute() throws EmailAddressIllegalExecption {
        if (!toEmail.matches("[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]+$"))
            throw new EmailAddressIllegalExecption();

        MailUtil.sendMail(toEmail,content);
        System.out.println("here");
    }

    @Override
    public void run() {
        try {
            this.execute();
            System.out.println(toEmail);
            System.out.println(content);
        } catch (EmailAddressIllegalExecption e) {
            System.out.println(e.getMessage());
            logger.debug(e.getMessage(),e);
        }
    }
}
