package com.project.system.service.impl;

import com.project.system.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {



    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendVerificationCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("【智能化在线教学支持服务平台学生中心系统】登录验证码");
        message.setText("您的验证码是：" + code + "，有效期5分钟。");
        mailSender.send(message);
    }
    public void sendEmail(String to, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("【智能化在线教学支持服务平台学生中心系统】教师入驻申请结果");
        message.setText(content);
        mailSender.send(message);
    }
}
