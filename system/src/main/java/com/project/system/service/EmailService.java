package com.project.system.service;

public interface EmailService {
    void sendVerificationCode(String to, String code);
    void sendEmail(String to, String content);
}
