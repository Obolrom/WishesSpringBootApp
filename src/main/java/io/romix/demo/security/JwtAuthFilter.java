package io.romix.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  @Value("${jwt.secret}")
  private String secret;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {
    try {
      BearerTokenAuthenticationConverter converter =
          new BearerTokenAuthenticationConverter(new JwtHandler(secret));

      Authentication authentication = converter.convert(request);

      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (Exception e) {
      log.error("Could not set user authentication in security context", e);
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
    final AntPathMatcher matcher = new AntPathMatcher();
    return Arrays.stream(SecurityConfig.PUBLIC_ENDPOINTS).toList().stream()
        .anyMatch(pub -> matcher.match(pub, request.getRequestURI()));
  }
}
