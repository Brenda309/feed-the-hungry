package com.brenda.FeedTheHungry.auth;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.brenda.FeedTheHungry.config.JwtSerive;
import com.brenda.FeedTheHungry.model.user.Role;
import com.brenda.FeedTheHungry.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
  private final UserRepository userRepository;  
private final PasswordEncoder passwordEncoder;
private final JwtSerive jwtSerive;
    
public AuthenticationResponse register(RegisterRequest request) {
var user = user.build()
.firstname(request.getFirstname())
.lastname(request.getLastname())
.email(request.getClass())
.password(passwordEncoder.encode(request.getPassword()))
.role(Role.USER)
.build();
userRepository.save(user);
 String jwtToken = jwtSerive.generateToken(user);
    return AuthenticationResponse.builder().token(jwtToken).build();
 
}  

public AuthenticationResponse authenticate(AuthenticationRequest request) {
    return null;
}

}
