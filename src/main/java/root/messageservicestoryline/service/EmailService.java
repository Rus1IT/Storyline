package root.messageservicestoryline.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import root.messageservicestoryline.dto.EmailDTO;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(EmailDTO emailDTO) {
        if(emailDTO == null){
            throw new NullPointerException("emailDTO is null");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDTO.to());
        message.setSubject(emailDTO.subject());
        message.setText(emailDTO.body());
        mailSender.send(message);
    }
}
