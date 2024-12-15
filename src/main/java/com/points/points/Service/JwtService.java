package com.points.points.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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










    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); //If you'll see the createToken method so subject claim we have set as username
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) { //This method will load all the claims and it will find token and return it back
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Date extractExpiration(String token) { //While creating token i.e. createToken method we have set the expiration in claims
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey()) // Set the signing key to verify the signature
                .build()
                .parseClaimsJws(token)      // Parses and validates the token's signature
                .getBody();                 // Extracts the claims (payload) if the token is valid

//        How It Works:
//        parseClaimsJws:
//
//        This method validates the token's structure and verifies its signature using the signing key provided by setSigningKey(getSignKey()).
//        Key Used for Signature Validation:
//
//        The getSignKey() method decodes the SECRET and creates a Key object using Keys.hmacShaKeyFor().
//                This key is the same one used when the token was originally created, ensuring that only tokens signed with the correct secret can pass validation.
//                What Happens During parseClaimsJws:
//
//        The library decodes the JWT's header, payload, and signature.
//        It recalculates the signature using the signing key and compares it with the token's provided signature.
//        If the signatures don't match, it throws an exception, indicating the token is invalid or tampered with.
    }


    private Boolean isTokenExpired(String token) {
        System.out.println("expirrrrrrrreddddddd "+ token);
        System.out.println(extractExpiration(token).before(new Date()));
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        System.out.println("validate token: " + username);
        // Compare the extracted username with the provided user's username and check if the token is not expired
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }



}
