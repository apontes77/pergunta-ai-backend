package br.com.pucgo.perguntaai.services;

import br.com.pucgo.perguntaai.models.User;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;

import javax.persistence.*;
import java.util.Date;
import java.util.Locale;

public class PasswordResetService {
    
    public SimpleMailMessage constructResetTokenEmail(
            String contextPath, Locale locale, String token, User user) {
        String url = contextPath + "/user/changePassword?token=" + token;
        String message = "messages.getMessage(";
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body,
                                             User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom("perguntaaipolitecnica@gmail.com");
        return email;
    }

}