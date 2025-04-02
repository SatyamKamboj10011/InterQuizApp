package com.satyam.FinalProjectBackend.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

        public void sendEmail (String to, String subject, String body){
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(body);

                mailSender.send(message);
                System.out.println("✅ Email sent successfully to: " + to);
            } catch (MessagingException e) {
                System.err.println("❌ Error sending email: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("❌ General error: " + e.getMessage());
            }

        }
    public void sendPasswordResetEmail(String to, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("Click this link to reset your password: " + link);
        mailSender.send(message);
    }
    }
