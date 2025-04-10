package com.brenda.FeedTheHungry.registration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final RegistrationService registrationService;
    
    @Value("${admin.registration.secret-key}")
    private String expectedKey;

    @PostMapping("/register")
    public ResponseEntity<String> registerAdmin(
            @RequestHeader("X-Admin-Key") String providedKey,
            @RequestBody RegistrationRequestAd request) {
        
        if (!expectedKey.equals(providedKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                   .body("Invalid admin key");
        }
        
        try {
            String result = registrationService.registerAdmin(request);
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostConstruct
public void init() {
    System.out.println("Loaded admin key: " + expectedKey); 
}
@GetMapping("/check-key")
public String checkKey() {
    return "Key loaded: " + (expectedKey != null ? "YES" : "NO");
}
}