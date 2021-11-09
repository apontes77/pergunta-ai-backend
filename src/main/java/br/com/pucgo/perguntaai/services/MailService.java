package br.com.pucgo.perguntaai.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMailMessage(
            final SimpleMailMessage simpleMailMessage) {

        this.mailSender.send(simpleMailMessage);
    }

    public String crateBody(final String token, final String nameUser)
    {
        String body = "Olá" + nameUser + ", \n" +
                " Clique no link para redefinir sua senha: https://pergunta-ai.vercel.app/forgot-password?token=" +token;
        return body;
    }
}