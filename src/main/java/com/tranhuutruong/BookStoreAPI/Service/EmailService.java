package com.tranhuutruong.BookStoreAPI.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    private JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender)
    {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(MimeMessage email)
    {
        javaMailSender.send(email);
    }

    @Async
    public void sendEmailWithAttachment(String fromEmail, String recipientEmail, String subject, String text, InputStreamSource attachment) {
            MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(recipientEmail);
            helper.setFrom(fromEmail);
            helper.setSubject(subject);
            helper.setText(text, false);
            helper.addAttachment("E-invoice.pdf", attachment);
        };
        javaMailSender.send(preparator);
    }
}
