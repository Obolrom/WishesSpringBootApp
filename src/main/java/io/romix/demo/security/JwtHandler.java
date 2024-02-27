package io.romix.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.romix.demo.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
public class JwtHandler {
  private final String secret;

  public VerificationResult check(String accessToken) {
    return verify(accessToken);
  }

  private VerificationResult verify(String accessToken) {
    Claims claims = getClaimsFromToken(accessToken);
    final Date expirationDate = claims.getExpiration();

    if (expirationDate.before(new Date())) {
      throw new CustomException("Token expired", HttpStatus.UNAUTHORIZED);
    }

    return new VerificationResult(claims, accessToken);
  }

  private Claims getClaimsFromToken(String accessToken) {
    return Jwts.parser()
        .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
        .parseClaimsJws(accessToken)
        .getBody();
  }

  public static class VerificationResult {
    public Claims claims;
    public String token;

    public VerificationResult(Claims claims, String token) {
      this.claims = claims;
      this.token = token;
    }
  }
}
