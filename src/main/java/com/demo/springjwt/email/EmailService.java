package com.demo.springjwt.email;

import com.demo.springjwt.constant.EmailConstant;
import com.sun.mail.smtp.SMTPTransport;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

import static com.demo.springjwt.constant.EmailConstant.*;

@Service
@RequiredArgsConstructor
public class EmailService implements EmailSender{

    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    @Override
    @Async
    public void send(String to, String email) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your account");
            helper.setFrom("aungthetko@hotguy.com");
            mailSender.send(mimeMessage);
        }catch (MessagingException e){
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }

//    private Session getEmailSession(){
//        Properties properties = System.getProperties();
//        properties.put(SMTP_HOST, GMAIL_SMTP_SERVE);
//        properties.put(SMTP_AUTH, true);
//        properties.put(SMTP_PORT, DEFAULT_PORT);
//        properties.put(SMTP_STARTTLS_ENABLE, true);
//        properties.put(SMTP_STARTTLS_REQUIRED, true);
//        return Session.getInstance(properties, null);
//    }
//
//    private Message createEmail(String firstName, String email, String password){
//        Message message = new MimeMessage(getEmailSession());
//        try{
//            message.setFrom(new InternetAddress(FROM_EMAIL));
//            message.setRecipients(Message.RecipientType.TO,
//                    InternetAddress.parse(email, false));
//            message.setRecipients(Message.RecipientType.TO,
//                    InternetAddress.parse(CC_MAIL, false));
//            message.setSubject(EMAIL_SUBJECT);
//            message.setText("Hello " + firstName + ", \n \n Your new password is : " + password
//            + "\n \n The Support Team");
//            message.setSentDate(new Date());
//            message.saveChanges();
//        }catch (MessagingException e){
//            e.printStackTrace();
//        }
//        return message;
//    }
//
//    public void sendNewPasswordToEmail(String firstName, String email, String password){
//        Message message = createEmail(firstName, email, password);
//        try{
//            SMTPTransport smtpTransport =
//                    (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
//            smtpTransport.connect(GMAIL_SMTP_SERVE, USERNAME, PASSWORD);
//            smtpTransport.sendMessage(message, message.getAllRecipients());
//            smtpTransport.close();
//        }catch (NoSuchProviderException e){
//            e.printStackTrace();
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }

//    }
}
