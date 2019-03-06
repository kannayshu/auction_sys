

package com.auction_sys.util;

import com.auction_sys.service.impl.ProductServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

@Slf4j
public class MailUtil {

    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);
    private static String fromEmail = PropertiesUtil.getProperty("mail_util.account");
    private static String password = PropertiesUtil.getProperty("mail_util.password");
    private static String encoding = PropertiesUtil.getProperty("mail_util.encoding");
    public static boolean sendMail(String to, String content) {
        try {
            Properties props = new Properties();
            props.put("username", fromEmail);
            props.put("password", password);
            props.put("mail.transport.protocol", "smtp" );
            props.put("mail.smtp.host", "smtp.163.com");

            Session mailSession = Session.getDefaultInstance(props);

            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject("激活邮件");
            msg.setContent(content,encoding);
            msg.saveChanges();

            Transport transport = mailSession.getTransport("smtp");
            transport.connect(props.getProperty("mail.smtp.host"), props
                    .getProperty("username"), props.getProperty("password"));
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            logger.debug(e.getMessage(),e);
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        sendMail("liudaxx@126.com","!!!!");
    }
}