package com.xenocrm.communication.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * EmailDispatchService — Handles sending actual emails via JavaMailSender.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailDispatchService {

    private final JavaMailSender mailSender;

    /**
     * Sends an HTML email.
     *
     * @param to          The recipient's email address
     * @param subject     The subject of the email
     * @param htmlContent The HTML body of the email
     */
    public void sendEmail(String to, String subject, String htmlContent) {
        log.info("Sending email to {} with subject: {}", to, subject);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // We must set a From address matching the authenticated SMTP user to avoid spoofing rejections by Gmail
            helper.setFrom("kingjames.08623@gmail.com"); 
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Successfully sent email to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
