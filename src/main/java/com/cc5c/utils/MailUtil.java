package com.cc5c.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.UnsupportedEncodingException;

@Component
public class MailUtil {
    @Value("${spring.mail.username}")
    private String sender;
    @Value("${spring.mail.username}")
    private String cc;

    //后加的防止题目过长并且进行全局定义
    static {
        System.setProperty("mail.mime.splitlongparameters", "false");
        System.setProperty("mail.mime.charset", "UTF-8");
    }

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMailWithFile(String text, String title, String attachmentName, File file, String... tos) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(sender);
        helper.setCc(cc);
        helper.setTo(tos);
        helper.setText(text);
        helper.setSubject(title);
        helper.addAttachment(MimeUtility.encodeWord(attachmentName), file);
        javaMailSender.send(message);
    }

    public void sendMailWithText(String text, String title, String... tos) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setCc(cc);
        message.setTo(tos);
        message.setSubject(title);
        message.setText(text);
        javaMailSender.send(message);
    }
}
