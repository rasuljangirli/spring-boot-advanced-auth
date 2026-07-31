package com.core.identity.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import com.core.identity.exception.EmailSendException;
import com.core.identity.service.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    @Async
    public void sendEmail(String to, String subject, String body) {

       try{
           MimeMessage mimeMessage = mailSender.createMimeMessage();
           MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

           String htmlContent = "<p style='font-size:14px;'>Təhlükəsizliyiniz üçün bu kodu kənar şəxslərlə paylaşmayın:</p>"
                   + "<h1 style='font-size:24px; font-weight:bold;'>" + body + "</h1>";

           helper.setTo(to);
           helper.setSubject(subject);
           helper.setText(htmlContent,true);

           mailSender.send(mimeMessage);

       }catch (MessagingException e){
           throw new EmailSendException();
       }
    }
}
