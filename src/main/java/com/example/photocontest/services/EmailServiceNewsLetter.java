package com.example.photocontest.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceNewsLetter {

    private final JavaMailSender javaMailSender;

    public EmailServiceNewsLetter(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendNewsletterEmail(String emailTo) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String subject = "Welcome to PhotoContestAI - Newsletter Subscription";
            String content = "<h3>Thank you for subscribing to PhotoContestAI Newsletter!</h3>"
                    + "<p>We are excited to keep you updated with the latest contests, tips, and photography resources.</p>"
                    + "<p><b>What to expect:</b></p>"
                    + "<ul>"
                    + "<li>Regular updates on the newest photography contests.</li>"
                    + "<li>Tips and tutorials to improve your photography skills.</li>"
                    + "<li>Inspiration from other talented photographers in our community.</li>"
                    + "</ul>"
                    + "<p>If you have any questions or need support, feel free to reach out to us at support@photocontestai.com.</p>"
                    + "<p>Thank you for being a part of PhotoContestAI!</p>"
                    + "<p>Best regards,<br>The PhotoContestAI Team</p>"
                    + "<a href=\"http://localhost:8080\">Visit PhotoContestAI</a>";

            // Настройки за имейл
            helper.setFrom("photocontestai@hotmail.com");  // This should match the authenticated email in application.properties
            helper.setTo(emailTo);
            helper.setSubject(subject);
            helper.setText(content, true); // Включваме HTML съдържанието

            // Изпращане на имейла
            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}