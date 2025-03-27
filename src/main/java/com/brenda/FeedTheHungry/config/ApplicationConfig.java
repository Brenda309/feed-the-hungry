package com.brenda.FeedTheHungry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.brenda.FeedTheHungry.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    @Bean
public UserDetailsService userDetailsService(){
return username -> userRepository.findByEmail(username)
.orElseThrow(() -> new UsernameNotFoundException("user not found"));
}

@Bean
public AuthenticationProvider authenticationProvider(){
    
}
}
