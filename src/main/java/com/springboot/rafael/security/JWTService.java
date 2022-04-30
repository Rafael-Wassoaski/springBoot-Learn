package com.springboot.rafael.security;

import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import com.springboot.rafael.domain.entity.CustomUser;
import org.springframework.beans.factory.annotation.Value;

@Service
public class JWTService {

    @Value("${security.jwt.expiration}")
    private String expiration;

    @Value("${security.jwt.signatureKey}")
    private String signatureKey;

    public String generateToken(CustomUser user){
        Long expString = Long.valueOf(this.expiration);
        LocalDateTime dateHourExpiration = LocalDateTime.now().plusMinutes(expString);
        Instant instant = dateHourExpiration.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);

        HashMap<String, Object> claims = new HashMap<>();
        // claims.put("email", "teste@email.com");
        // claims.put("roles", "ADMIN");

        return Jwts
                .builder()
                .setSubject(user.getUsername())
                .setExpiration(date)
                // .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, this.signatureKey)
                .compact();
    }

    private Claims getClaims(String token) throws ExpiredJwtException{
        return Jwts
        .parser()
        .setSigningKey(this.signatureKey)
        .parseClaimsJws(token)
        .getBody();
    }


    public boolean isTokenValid(String token){
        try {
            Claims claims = this.getClaims(token);
            Date expirationDate = claims.getExpiration();

            LocalDateTime exDateTime = expirationDate
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

            return !LocalDateTime.now().isAfter(exDateTime);
        } catch (Exception e) {
            System.out.println("Error: "+ e);
            return false;
        }
    }

    public String getUsername(String token) throws ExpiredJwtException{
        Claims claims = this.getClaims(token);

        return claims.getSubject();
    }

}
