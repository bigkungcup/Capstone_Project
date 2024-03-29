package sit.cp23ej2.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String template, Map<String, Object> variables)
            throws AddressException, MessagingException {
        // SimpleMailMessage message = new SimpleMailMessage();
        MimeMessage message = mailSender.createMimeMessage();
        // message.setFrom(new InternetAddress("sender@example.com"));

        message.setFrom(new InternetAddress("capstone23ej2@gmail.com"));
        message.setRecipients(MimeMessage.RecipientType.TO, to);
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
                    "<p style=\"color: black;\">Hello, This is an email for resetting your password.</p> <br/>" +
                    "<p style=\"color: black;\">If you requested a password reset for <strong>" + email
                    + "</strong> account, please use the password below to login and reset your password.</p>" +
                    "<p style=\"color: black;\">If you did not request a password reset, please contact the admin via capstone23ej2@gmail.com </p>"
                    +
                    "<p style=\"color: black; padding-left: 20px;\"><strong>Your email is:</strong> " + email + "</p>" +
                    "<p style=\"color: black; padding-left: 20px;\"><strong>Your password is:</strong> " + password + "</p> <br/>" +
                    "<p style=\"color: black;\">Best regards,<br/>Bannarug Admin</p>" +
                    "</body></html>";

            // message.setText(emailBody);
            message.setContent(emailBody, "text/html; charset=utf-8");

            mailSender.send(message);
        } else if (template.equals("System Maintenance")) {
            String detail = (String) variables.get("detail");
            String emailBody = "<html><body>" +
                    "<p style=\"color: black;\">Dear our user, </p> <br/>" +
                    "<p style=\"color: black;\">" + detail + "</p>" +
                    "<p style=\"color: black;\">Our website will not be available at that time. We apologize for any inconvenience.</p> <br/>"
                    +
                    "<p style=\"color: black;\">Best regards,<br/>Bannarug Admin</p>" +
                    "</body></html>";

            // message.setText(emailBody);
            message.setContent(emailBody, "text/html; charset=utf-8");

            mailSender.send(message);
        } else if (template.equals("Delete Account")) {
            String username = (String) variables.get("username");
            String email = (String) variables.get("email");

            String emailBody = "<html><body>" +
                    "<p style=\"color: black;\">Hello, </p> <br/>" +
                    "<p style=\"color: black;\">This message confirm that your Bannarug account was deleted due to your inappropriate action in our website.</p>"
                    +
                    "<p style=\"color: black; padding-left: 20px;\"> <u>Username</u>: " + username + "<br/>" +
                    "<u>Review Email</u>: " + email +
                    "</p>" +
                    "<p style=\"color: black;\">If you have any question, you can contact admin via capstone23ej2@gmail.com </p> <br/>"
                    +
                    "<p style=\"color: black;\">Best regards,<br/>Bannarug Admin</p>" +
                    "</body></html>";

            // message.setText(emailBody);
            message.setContent(emailBody, "text/html; charset=utf-8");

            mailSender.send(message);
        } else if (template.equals("Delete Review")) {
            String bookName = (String) variables.get("bookName");
            String reviewName = (String) variables.get("reviewName");
            String reviewDetail = (String) variables.get("reviewDetail");

            String emailBody = "<html><body>" +
                    "<p style=\"color: black;\">Hello, </p> <br/>" +
                    "<p style=\"color: black;\">This message confirm that your review in Bannarug was deleted due to your inappropriate action in our website.</p>"
                    +
                    "<p style=\"color: black; padding-left: 20px;\"> <u>Book</u>:" + bookName + "<br/>" +
                    "<u>Review Title</u>: " + reviewName + "<br/>" +
                    "<u>Review Detail</u>: " + reviewDetail +
                    "</p>" +
                    "<p style=\"color: black;\">If you have any question, you can contact admin via capstone23ej2@gmail.com </p> <br/>"
                    +
                    "<p style=\"color: black;\">Best regards,<br/>Bannarug Admin</p>" +
                    "</body></html>";

            // message.setText(emailBody);
            message.setContent(emailBody, "text/html; charset=utf-8");

            mailSender.send(message);
        }

    }

}
