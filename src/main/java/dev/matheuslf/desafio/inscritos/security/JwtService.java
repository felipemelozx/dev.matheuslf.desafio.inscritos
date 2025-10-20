package dev.matheuslf.desafio.inscritos.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import java.time.Instant;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private String secret = "secret";

  public String generateAccessToken(String email) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT.create()
          .withIssuer("desafio-inscritos")
          .withSubject(email)
          .withExpiresAt(getAccessTokenExpiry())
          .sign(algorithm);
    } catch (Exception e) {
      throw new JWTCreationException("Error while generating JWT token.", e);
    }
  }

  protected DecodedJWT validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT.require(algorithm)
          .build()
          .verify(token);
    } catch (JWTVerificationException e) {
      return null;
    }
  }

  protected Instant getAccessTokenExpiry() {
    return Instant.now().plusSeconds(15L * 60);
  }

  public String extractEmail(String token) {
    DecodedJWT jwt = validateToken(token);
    return jwt != null ? jwt.getSubject() : null;
  }
}