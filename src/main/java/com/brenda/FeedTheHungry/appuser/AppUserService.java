package com.brenda.FeedTheHungry.appuser;


import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.brenda.FeedTheHungry.entity.Beneficiary;
import com.brenda.FeedTheHungry.entity.Donor;
import com.brenda.FeedTheHungry.entity.Volunteer;
import com.brenda.FeedTheHungry.registration.RegistrationRequest;
import com.brenda.FeedTheHungry.registration.token.ConfirmationToken;
import com.brenda.FeedTheHungry.registration.token.ConfirmationTokenService;
import com.brenda.FeedTheHungry.repository.BeneficiaryRepository;
import com.brenda.FeedTheHungry.repository.DonorRepository;
import com.brenda.FeedTheHungry.repository.RoleRepository;
import com.brenda.FeedTheHungry.repository.UserRepository;
import com.brenda.FeedTheHungry.repository.VolunteerRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
   //private static final String ADMIN_EMAIL = "admin@gmail.com";
    private final static String USER_NOT_FOUND = "user with email %s not found";

    private final UserRepository userRepository;
    private final DonorRepository donorRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final VolunteerRepository volunteerRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }

    public String signUpUser(AppUser appUser, String fullName, String phoneOrLocation) {
        boolean userExists = userRepository.findByEmail(appUser.getEmail()).isPresent();
        
        if (userExists) {
            throw new IllegalStateException("Email already taken");
        }
    
        Role role = appUser.getRole();
        if (role == null) {
            role = roleRepository.findByRoleName(AppUserRole.DONOR)
                    .orElseThrow(() -> new IllegalStateException("Default role DONOR not found"));
            appUser.setRole(role);
        }
        
        AppUserRole userRole = role.getRoleName();
    
        // Special handling only for ADMIN
       // Special handling only for ADMIN
    if (userRole == AppUserRole.ADMIN) {
        if (userRepository.existsByRoleName(userRole)) {
            throw new IllegalStateException("Admin account already exists");
        }
        if (!appUser.getEmail().equalsIgnoreCase("admin@feedthehungry.com")) {
            throw new IllegalStateException("Only the predefined admin email can register as ADMIN");
        }
        appUser.setEnabled(true);
    }
    
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        userRepository.save(appUser);
    
        // Handle all regular roles (DONOR, BENEFICIARY, VOLUNTEER) the same way
        switch (userRole) {
            case DONOR -> {
                Donor donor = new Donor();
                donor.setFullName(fullName);
                donor.setPhoneNumber(phoneOrLocation);
                donor.setAppUser(appUser);
                donorRepository.save(donor);
            }
            case BENEFICIARY -> {
                Beneficiary beneficiary = new Beneficiary();
                beneficiary.setFullName(fullName);
                beneficiary.setLocation(phoneOrLocation);
                beneficiary.setAppUser(appUser);
                beneficiaryRepository.save(beneficiary);
            }
            case VOLUNTEER -> {
                Volunteer volunteer = new Volunteer();
                volunteer.setFullName(fullName);
                volunteer.setAssignedZone(phoneOrLocation); // Using phoneOrLocation as zone
                volunteer.setAppUser(appUser);
                volunteerRepository.save(volunteer);
            }
            case ADMIN -> {
                // No additional entity needed for admin
            }
            default -> throw new IllegalStateException("Unsupported role");
        }
    
        // Skip confirmation for admin
        if (userRole == AppUserRole.ADMIN) {
            return "admin_auto_enabled";
        }
    
        // Standard confirmation for all other roles
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
    
        return token;
    }

    public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }

    @Transactional
public String registerAdmin(RegistrationRequest request) {
    Role adminRole = roleRepository.findByRoleName(AppUserRole.ADMIN)
            .orElseThrow(() -> new IllegalStateException("ADMIN role not found"));

    AppUser adminUser = new AppUser(
        request.getFirstName(),
        request.getLastName(),
        request.getEmail(),
        request.getPassword(),
        adminRole
    );

    return signUpUser(adminUser, request.getFirstName(), "");
}
}