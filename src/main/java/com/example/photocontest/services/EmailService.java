package com.example.photocontest.services;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleEmail(String emailTo,String username) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String subject = "Welcome to PhotoContestAI - Your Journey Begins!";
            String content = "<h3>Dear " + username + ",</h3>"
                    + "<p>Welcome to PhotoContestAI! We're thrilled to have you join our growing community of photography enthusiasts.</p>"
                    + "<p><b>What is PhotoContestAI?</b></p>"
                    + "<p>PhotoContestAI, where \"AI\" stands for Alexander and Ivan, is more than just a platform; it's a place where creativity meets competition. "
                    + "Founded by passionate photographers, Alexander and Ivan, our platform offers you the opportunity to showcase your skills, learn from others, and become a part of a supportive community that values the art of photography.</p>"
                    + "<p><b>What’s Next?</b></p>"
                    + "<ul>"
                    + "<li><b>Explore Contests:</b> Dive into the latest photography contests and start submitting your work. Each contest offers a unique theme and the chance to win exciting rewards.</li>"
                    + "<li><b>Connect with Others:</b> Follow other photographers, like their work, and get inspired. The PhotoContestAI community is here to support and encourage your creative journey.</li>"
                    + "<li><b>Grow Your Skills:</b> Take advantage of our resources, from tutorials to feedback from seasoned photographers. Whether you’re a beginner or a pro, there’s always something new to learn.</li>"
                    + "</ul>"
                    + "<p>If you have any questions or need assistance, our support team is just an email away. Contact us at support@photocontestai.com, and we’ll be happy to help.</p>"
                    + "<p>Thank you for choosing PhotoContestAI. We can't wait to see the amazing photos you'll share with us!</p>"
                    + "<p>Happy shooting!</p>"
                    + "<p>Best regards,<br>The PhotoContestAI Team<br>Alexander and Ivan</p>";


            helper.setFrom("photocontestai@hotmail.com");  // This should match the authenticated email in app.properties
            helper.setTo(emailTo);
            helper.setSubject(subject);
            helper.setText(content, true);

            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}