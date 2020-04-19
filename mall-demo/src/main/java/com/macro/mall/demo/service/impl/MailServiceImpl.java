package com.macro.mall.demo.service.impl;

import com.macro.mall.demo.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @author Louis
 * @description
 * @create 2020-04-18 14:09
 */
@Service
@Slf4j
public class MailServiceImpl implements MailService {

    @Resource
    JavaMailSender javaMailSender;

    @Override
    public void sendSimpleMail(String from, String to, String cc, String subject, String content) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        if (StringUtils.isNotEmpty(cc)) {
            simpleMailMessage.setCc(cc);
        }
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        try {
            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            log.error("发送邮件失败：{}", e.getMessage());
        }
    }

    /**
     * 代附件的邮件
     *
     * @param from
     * @param to
     * @param subject
     * @param content
     * @param file
     */
    @Override
    public void sendAttachFileMail(String from, String to, String subject, String content, File file) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content);
            mimeMessageHelper.addAttachment(file.getName(), file);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("发送邮件失败：{}", e.getMessage());
        }
    }

    /**
     * 使用thymeleaf模板
     *
     * @param from
     * @param to
     * @param subject
     * @param context
     */
    @Override
    public void sendHtmlMail(String from, String to, String cc, String subject, String context, File file) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            if (StringUtils.isNotEmpty(cc)) {
                mimeMessageHelper.setCc(cc);
            }
            mimeMessageHelper.setSubject(subject);

            mimeMessageHelper.setText(context, true);

            if (file != null) {
                mimeMessageHelper.addAttachment(file.getName(), file);
            }
            try {
                javaMailSender.send(mimeMessage);
            } catch (MailException e) {
                log.error("发送邮件失败：{}", e.getMessage());
            }
        } catch (MessagingException e) {
            log.error("发送邮件失败：{}", e.getMessage());
        }
    }
}
