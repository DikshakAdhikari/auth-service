package com.points.points.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {

    public static final String SECRET = "4B6F18D3B89E3A4A1E3EBAF1B7C7DFA5C6D3F5A1A0A5D1F7F6E0C3A1B2B3C4D5"; //Generated by Encryption key generator, it's in hexa format

    public String generateToken(String userName) { //1 -> As we know that in Jwt has 3 components i.e. header, payload and verify signature. All the components are called as a Claims in Jwt
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String userName) {
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userName)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error creating JWT token", ex);
        }

    }

    public Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET); //Creating my own secret, i.e. base64 encoded key in bytes format
        return Keys.hmacShaKeyFor(keyBytes); // This will give us the signed key based on our own secret which is defined above in variable SECRET
    }

}
