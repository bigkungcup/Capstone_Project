package sit.cp23ej2.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String template, Map<String, Object> variables) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);

        if (template.equals("Change Pasword")) {
            String email = (String) variables.get("email");
            String password = (String) variables.get("password");
            // message.setText("Hello, This is an email for reset your password. \n" +
            // "If you requested a password reset for" + variables.get("email") + " account,
            // please use the password below to login and go to reset your password. \n" +
            // "If you did not request a password reset, please contact admin. \n" +
            // "Your email is: " + variables.get("email") + "\n" +
            // "Your password is: " + variables.get("password") + "\n" +
            // "Best regards, \n" +
            // "Bannarug Admin");
            String emailBody = "<html><body>" +
                    "<p>Hello,</p>" +
                    "<p>This is an email for resetting your password.</p>" +
                    "<p>If you requested a password reset for <strong>" + email
                    + "</strong> account, please use the password below to login and reset your password.</p>" +
                    "<p>If you did not request a password reset, please contact the admin.</p>" +
                    "<p><strong>Your email is:</strong> " + email + "</p>" +
                    "<p><strong>Your password is:</strong> " + password + "</p>" +
                    "<p>Best regards,<br/>Bannarug Admin</p>" +
                    "</body></html>";
            message.setText(emailBody);
        }else{
            message.setText(template);
        }


        mailSender.send(message);
    }

}
