package br.com.pucgo.perguntaai.services;

import br.com.pucgo.perguntaai.models.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

@Service
@AllArgsConstructor
public class MailService {

    public void sendMail(final User user) throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("perguntaaipolitecnica@gmail.com", "perguntaa1");
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("perguntaaipolitecnica@gmail.com", false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
        msg.setSubject("Redefina sua senha do fórum Pergunta Aí!");
        msg.setContent("Olá "+user.getName()+ "! Clique no link para redefinir sua senha: \n<a href='https://pergunta-ai.vercel.app/set-password?id="+user.getId().toString()+"'>Clique aqui</a>",
                "text/html");
        msg.setSentDate(new Date());


        Transport.send(msg);
    }
}