package com.kellot.spring_security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/private")
    public ResponseEntity<User> getPrivatePage(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            LOGGER.info("Authenticated username : {}", user.getUsername());
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(401).build();
    }
}
