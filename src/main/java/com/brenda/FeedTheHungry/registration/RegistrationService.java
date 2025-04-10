package com.brenda.FeedTheHungry.registration;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.brenda.FeedTheHungry.appuser.AppUser;
import com.brenda.FeedTheHungry.appuser.AppUserRole;
import com.brenda.FeedTheHungry.appuser.AppUserService;
import com.brenda.FeedTheHungry.appuser.Role;
import com.brenda.FeedTheHungry.mail.EmailBuilder;
import com.brenda.FeedTheHungry.mail.EmailSender;
import com.brenda.FeedTheHungry.mail.EmailValidater;
import com.brenda.FeedTheHungry.registration.token.ConfirmationToken;
import com.brenda.FeedTheHungry.registration.token.ConfirmationTokenService;
import com.brenda.FeedTheHungry.repository.RoleRepository;
import com.brenda.FeedTheHungry.repository.UserRepository;


import java.time.LocalDateTime;

    @Service
    @RequiredArgsConstructor
    public class RegistrationService {
        private final EmailValidater emailValidater;
        private final AppUserService appUserService;
        private final RoleRepository roleRepository;
        private final UserRepository userRepository;
        private final ConfirmationTokenService confirmationTokenService;
        private final EmailSender emailSender;
          private final BCryptPasswordEncoder bCryptPasswordEncoder;
    
        public String register(RegistrationRequest request) {
            validateEmail(request.getEmail());
        
            Role roleEntity = roleRepository.findByRoleName(request.getRole())
                    .orElseThrow(() -> new IllegalStateException("Role not found"));
        
            if (request.getRole() == AppUserRole.ADMIN) {
                throw new IllegalStateException("Admin registration not allowed through this endpoint");
            }
        
            AppUser newUser = new AppUser(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                roleEntity
            );
        
            String token = appUserService.signUpUser(
                newUser,
                request.getFirstName(),
                request.getPhoneOrLocation()
            );
        
            sendRoleSpecificEmail(
                request.getEmail(), 
                request.getFirstName(), 
                token, 
                request.getRole()
            );
        
            return token;
        }
    
        private void sendRoleSpecificEmail(String email, String name, String token, AppUserRole role) {
            String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
            
            try {
                String emailContent = EmailBuilder.buildEmailContent(name, link, role);
                String subject = EmailBuilder.getEmailSubject(role);
                emailSender.send(email, emailContent, subject);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to send email to " + email, e);
            }
        }
    
        @Transactional
        public String confirmToken(String token) {
            ConfirmationToken confirmationToken = confirmationTokenService
                    .getToken(token)
                    .orElseThrow(() -> new IllegalStateException("Token not found"));
    
            if (confirmationToken.getConfirmedAt() != null) {
                throw new IllegalStateException("Email already confirmed");
            }
    
            if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
                throw new IllegalStateException("Token expired");
            }
    
            confirmationTokenService.setConfirmedAt(token);
            appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());
    
            return "confirmed";
        }
    
        private void validateEmail(String email) {
            if (email == null || email.isBlank()) {
                throw new IllegalStateException("Email cannot be empty");
            }
            if (!emailValidater.test(email)) {
                throw new IllegalStateException("Email not valid");
            }
        }
        
        @Transactional
        public String registerAdmin(RegistrationRequestAd request) {
            // Verify it's the predefined admin email
            if (!request.email().equalsIgnoreCase("admin@feedthehungry.com")) {
                throw new IllegalStateException("Invalid admin registration attempt");
            }
        
            // Check if admin already exists using the new method
            if (userRepository.existsByRoleName(AppUserRole.ADMIN)) {
                throw new IllegalStateException("Admin account already exists");
            }
        
            Role adminRole = roleRepository.findByRoleName(AppUserRole.ADMIN)
                    .orElseThrow(() -> new IllegalStateException("ADMIN role not configured"));
        
            AppUser adminUser = new AppUser(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.password(),
                adminRole
            );
        
            // Auto-enable admin account
            adminUser.setEnabled(true);
            
            String encodedPassword = bCryptPasswordEncoder.encode(adminUser.getPassword());
            adminUser.setPassword(encodedPassword);
            
            userRepository.save(adminUser);
            
            // Send admin welcome email
            sendAdminWelcomeEmail(adminUser.getEmail(), adminUser.getFirstName());
            
            return "Admin account created successfully";
        }
        
        private void sendAdminWelcomeEmail(String email, String name) {
            try {
                // Use the EmailBuilder's static method
                String emailContent = EmailBuilder.buildEmailContent(name, "", AppUserRole.ADMIN);
                String subject = "Admin Account Activated";
                emailSender.send(email, emailContent, subject);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to send admin welcome email", e);
            }
        }
    }