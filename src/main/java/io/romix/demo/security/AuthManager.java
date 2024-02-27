package io.romix.demo.security;

import io.romix.demo.CustomException;
import io.romix.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthManager implements AuthenticationManager {
  private final UserRepository userRepository;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();

    return userRepository.findById(principal.getId())
        .map(user -> {
          principal.setUsername(user.getUsername());
          return authentication;
//          return new CustomAuthentication(principal, authentication.getCredentials(), authentication.getAuthorities());
        })
        .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
  }
}
