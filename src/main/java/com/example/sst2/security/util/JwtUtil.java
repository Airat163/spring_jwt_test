package com.example.sst2.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String username) {
        return JWT
                .create()
                .withSubject("information")
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withIssuer("Airat")
                .withExpiresAt(Date.from(ZonedDateTime.now().plusMinutes(10).toInstant()))
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndReturnClaim(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("information")
                .withIssuer("Airat")
                .build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getClaim("username").asString();
    }
}
