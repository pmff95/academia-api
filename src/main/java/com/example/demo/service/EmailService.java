package com.example.demo.service;

import com.example.demo.common.config.email.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    public EmailService(JavaMailSender mailSender, MailProperties mailProperties) {
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;
    }

    public void enviarSenha(String destinatario, String senha) {
        enviarSenha(destinatario, senha, null);
    }

    public void enviarSenha(String destinatario, String senha, String academiaUuid) {
        if (destinatario == null || destinatario.isBlank()) {
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailProperties.getFrom());
        message.setTo(destinatario);
        message.setSubject("Senha de acesso");
        StringBuilder texto = new StringBuilder("Sua senha temporária é: ").append(senha);
        message.setText(texto.toString());
        mailSender.send(message);
    }
}
