package com.brenda.FeedTheHungry.config;

import java.security.Key;
import java.util.Base64.Decoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtSerive {

    private static final String SECRET_KEY = "ec75af48c9d84dd51bd9fcf33ca337f13903ffabc07e277bf0fe1cd2640c3441" ;
public String extractUsername(String token){
    return null
}
public <T> T extractClaims(String token, Function<Claims, T> claimResolver){
    final Claims claims = extractClaims(token);
    return claimResolver.apply(claims);
}

public String generateToken(UserDetails userDetails){
    return generateToken(new HashMap<>(), userDetails);
}
public String generateToken(
    Map<String, Object>
    extractClaims,
     UserDetails userDetails){

        return Jwts
        .builder()
        .setClaims(extractClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
        .signWith(getSignInkey(), SignatureAlgorithm.HS256)
        .compact();
}
private Claims extractClaims(String token){
    return Jwts
    .parserBuilder()
    .setSigningKey(getSignInkey())
        .build()
        .parseClaimsJws(token)
        .getBady();
    }
    
    private Key getSignInkey() {
         byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
         return Keys.hmacShaKeyFor(keyBytes);
    }
}
