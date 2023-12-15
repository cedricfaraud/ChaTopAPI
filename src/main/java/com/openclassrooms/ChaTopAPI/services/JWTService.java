package com.openclassrooms.ChaTopAPI.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JWTService {
    private JwtEncoder jwtEncoder;
    private String jwtKey = "DssAQuXrhqOJ3c3PbCUo8rZZjKkS3CJjQQZ4RfD0v7I=";

    public JWTService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(String email) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(email)
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
                .from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        String token = this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
        return token;
    }

    /**
     * Get the username from the JWT token.
     *
     * @param token The JWT token.
     * @return The username extracted from the token.
     */
    public String getUsername(String token) {

        Claims claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody();
        return claims.getSubject().split(",")[0];
    }
    /*
     * public Claims parseToken(String token) {
     * return Jwts.parserBuilder()
     * .setSigningKey(this.getKey())
     * .build()
     * .parseClaimsJws(token)
     * .getBody();
     * }
     * 
     * public String extractUsername(String token) {
     * return parseToken(token).getSubject();
     * }
     */
}
