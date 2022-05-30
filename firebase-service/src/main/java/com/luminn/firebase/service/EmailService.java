package com.luminn.firebase.service;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

@Component("emailService")
public class EmailService {

    @Value("${email_verify}")
    private String emailTemplateResetPassword;
    @Value("${email_verify}")
    private String emailTemplate;
    @Value("${emailTemplateFolder}")
    private String emailTemplateFolder;
    @Value("${sendingEmail}")
    private String sendingEmail;

    @Autowired
    private MessageByLocaleService messageservice;

    //@Autowired
    //&&private freemarker.template.Configuration freeMarker;

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);


    public void sendCCEmail(String sendFromEmail, String sendToEmail,String sendCCToEmail, String subject, String messageBody) {

        /*Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(sendFromEmail));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(sendToEmail));
            msg.addRecipient(Message.RecipientType.CC, new InternetAddress(sendCCToEmail));
            msg.setSubject(subject);
            msg.setText(messageBody);
            Transport.send(msg);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }*/
    }

    public void sendEmail(String sendFromEmail, String sendToEmail,String d, String subject, String messageBody) {
        Properties props = new Properties();
        /*Session session = Session.getDefaultInstance(props, null);

        System.out.println("sendFromEmail ===== " + sendFromEmail + "sendToEmail ========" + sendToEmail );

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(sendFromEmail));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(sendToEmail));
            msg.setSubject(subject);
            msg.setText(messageBody);
            Transport.send(msg);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }*/
    }

    public void sendHtmlEmail(String sendFromEmail, String sendToEmail,String senderEmail,
                              String subject, String htmlBody) {
        Properties props = new Properties();
       /* Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(sendFromEmail));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(sendToEmail));
            if(senderEmail != null)
                msg.addRecipient(Message.RecipientType.CC, new InternetAddress(senderEmail));
            msg.setSubject(subject);
            Multipart multipart = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlBody, "text/html");
            multipart.addBodyPart(htmlPart);
            msg.setContent(multipart);
            Transport.send(msg);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }*/
    }
}
