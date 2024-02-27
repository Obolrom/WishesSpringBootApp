package io.romix.demo.security;

import io.jsonwebtoken.Claims;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class UserAuthenticationBearer {

  public static Authentication create(@NonNull JwtHandler.VerificationResult verificationResult) {
    Claims claims = verificationResult.claims;
    String subject = claims.getSubject();
    String role = claims.get("role", String.class);
    String username = claims.get("username", String.class);
    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

    Long principalId = Long.parseLong(subject);
    CustomPrincipal principal = new CustomPrincipal(principalId, username);

    return new UsernamePasswordAuthenticationToken(principal, null, authorities);
  }
}
