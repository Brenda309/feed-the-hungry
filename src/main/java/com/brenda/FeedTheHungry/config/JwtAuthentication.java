package com.brenda.FeedTheHungry.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthentication extends OncePerRequestFilter{

// class to manipulate jwt service
private final JwtSerive jwtService; 

    @Override
    protected void doFilterInternal(
      @NonNull  HttpServletRequest request, 
      @NonNull HttpServletResponse response, 
      @NonNull FilterChain filterChain)
     throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
        filterChain.doFilter(request, response);
        return;
    }
  jwt = authHeader.substring(7); //bearer with space equals to 7
userEmail =  jwtService.extractUsername(jwt);// todo extract user email from jwt token
}
}
