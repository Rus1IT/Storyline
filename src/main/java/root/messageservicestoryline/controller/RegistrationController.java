package root.messageservicestoryline.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import root.messageservicestoryline.dto.UserRegistrationDTO;
import root.messageservicestoryline.service.UserManagementService;

import java.util.Map;

@RestController
@Slf4j
public class RegistrationController {

    @Autowired
    private UserManagementService userManagementService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register/user")
    public ResponseEntity<?> createUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        try {
            UserRegistrationDTO myUserRegistrationDTO = new UserRegistrationDTO(
                    userRegistrationDTO.username(), userRegistrationDTO.name(),
                    userRegistrationDTO.age(), passwordEncoder.encode(userRegistrationDTO.password()),
                    userRegistrationDTO.registration_account(), userRegistrationDTO.registration_type(),
                    userRegistrationDTO.role()
            );

            ResponseEntity<?> response = userManagementService.registerUser(myUserRegistrationDTO);

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            log.error("Error during user registration: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", "Unknown error during user registration",
                    "errorDetails", e.getMessage()
            ));
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable("username") String username){
        try {
            UserRegistrationDTO userDTO = userManagementService.getUser(username);
            log.info("User: {}", userDTO);
            return ResponseEntity.status(HttpStatus.OK).body(userDTO);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Unknown error during user registration:"+e.getMessage()));
        }
    }


}
