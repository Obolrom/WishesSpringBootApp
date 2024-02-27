package io.romix.demo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.romix.demo.CustomException;
import io.romix.demo.entity.UserEntity;
import io.romix.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class SecurityService {
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.issuer}")
  private String issuer;

  @Value("${jwt.expiration}")
  private Long expirationInSeconds;

  @Autowired
  public SecurityService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public TokenDetails authenticate(String username, String password) {
    Optional<UserEntity> userByUsername = userRepository.findByUsername(username);

    if (userByUsername.isPresent()) {
      UserEntity user = userByUsername.get();

      if (!passwordEncoder.matches(password, user.getPassword())) {
        throw new CustomException("Invalid password", HttpStatus.UNAUTHORIZED);
      }

      return generateToken(user).toBuilder()
          .userId(user.getId())
          .build();
    } else {
      throw new CustomException("User not found", HttpStatus.NOT_FOUND);
    }
  }

  private TokenDetails generateToken(UserEntity user) {
    Map<String, Object> claims = new HashMap<>() {{
      put("username", user.getUsername());
      put("role", user.getRole());
    }};

    return generateToken(claims, user.getId().toString());
  }

  private TokenDetails generateToken(Map<String, Object> claims, String subject) {
    long expirationTimeInMillis = expirationInSeconds * 1000L;
    Date expirationDate = new Date(new Date().getTime() + expirationTimeInMillis);

    return generateToken(expirationDate, claims, subject);
  }

  private TokenDetails generateToken(
      Date expirationDate, Map<String, Object> claims, String subject) {
    Date createdDate = new Date();
    String token = Jwts.builder()
        .setClaims(claims)
        .setIssuer(issuer)
        .setIssuedAt(createdDate)
        .setSubject(subject)
        .setId(UUID.randomUUID().toString())
        .setExpiration(expirationDate)
        .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes()))
        .compact();

    return TokenDetails.builder()
        .token(token)
        .issuedAt(createdDate)
        .expiresAt(expirationDate)
        .build();
  }
}
