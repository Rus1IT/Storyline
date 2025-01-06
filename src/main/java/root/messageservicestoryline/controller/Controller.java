package root.messageservicestoryline.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import root.messageservicestoryline.dto.EmailDTO;
import root.messageservicestoryline.dto.WhatsappMessageDTO;
import root.messageservicestoryline.email.EmailService;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    private final EmailService emailService;

    @Value("${messageWhatsappSenderPort.port}")
    private Integer messageWhatsappSenderPort;


    @PostMapping("/api/email/send")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailDTO emailDTO) {
        try {
            emailService.sendEmail(emailDTO);
            logger.info("Email sent successfully", emailDTO);
            return ResponseEntity.ok("Email sent successfully");
        }
        catch (Exception e) {
            logger.error("Failed to sent email " + emailDTO + " .Reason:" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email:" + emailDTO + " " + e.getMessage());
        }
    }

    @PostMapping("/api/whatsapp/send")
    public ResponseEntity<String> sendWhatsappMessage(@Valid @RequestBody WhatsappMessageDTO whatsappMessageDTO) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:" + messageWhatsappSenderPort + "/api/v1/sendMessageNow";
            logger.info("Sending request to url:" + url);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("number", whatsappMessageDTO.number());
            requestBody.put("message", whatsappMessageDTO.message());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok("Email sent successfully");
            } else {
                logger.error("Error from MessageSenderWhatsapp:" + response.getBody());
                return ResponseEntity.status(response.getStatusCode()).body("Failed to send message.Reason:" + response.getBody());
            }

        }
        catch (HttpServerErrorException | HttpClientErrorException e) {
            logger.error("HttpException error during communication with MessageSenderWhatsapp:" + e.getMessage(), e);
            return ResponseEntity.status(e.getStatusCode()).body("HttpException.Failed to send message.Reason:" + e.getMessage());
        }
        catch (ResourceAccessException e) {
            logger.error("messageService cannot connect to MessageSenderWhatsapp.Check MessageSenderWhatsapp available or not:" + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("MessageService cannot connect to MessageSenderWhatsapp.Failed to send email:" + whatsappMessageDTO + " " + e.getMessage());
        }
        catch (Exception e) {
            logger.error("Unexpected error during communication with MessageSenderWhatsapp.Reason:" + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error.Failed to send email:" + whatsappMessageDTO + " " + e.getMessage());
        }
    }
}
