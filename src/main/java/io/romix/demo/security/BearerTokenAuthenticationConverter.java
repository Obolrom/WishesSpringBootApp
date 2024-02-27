package io.romix.demo.security;

import io.romix.demo.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.util.function.Function;

@RequiredArgsConstructor
public class BearerTokenAuthenticationConverter implements AuthenticationConverter {
  private static final String BEARER_PREFIX = "Bearer ";
  private static final Function<String, String> getBearerValue =
      authHeader -> authHeader.substring(BEARER_PREFIX.length());
  private final JwtHandler jwtHandler;

  @Override
  public Authentication convert(HttpServletRequest request) {
    String extractedToken = extractToken(request);
    JwtHandler.VerificationResult check = jwtHandler.check(extractedToken);

    return UserAuthenticationBearer.create(check);
  }

  private String extractToken(HttpServletRequest request) {
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeader == null) {
      throw new CustomException("Authorization header is missing", HttpStatus.BAD_REQUEST);
    }
    return getBearerValue.apply(authHeader);
  }
}
