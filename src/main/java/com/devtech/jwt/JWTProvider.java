package com.devtech.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JWTProvider {
    //TODO key
    
    public String generateToken(String login) {
        Date date = Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Jwts.builder().setSubject(login).setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, "test").compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey("test").parseClaimsJws(token);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public String getLogin(String token) {
        Claims claims = Jwts.parser().setSigningKey("test").parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
