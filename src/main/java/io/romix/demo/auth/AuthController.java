package io.romix.demo.auth;

import io.romix.demo.controller.UserCreateRequest;
import io.romix.demo.security.SecurityService;
import io.romix.demo.security.TokenDetails;
import io.romix.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final SecurityService securityService;
  private final UserService userService;

  @PostMapping("/register")
  public void register(@RequestBody UserCreateRequest request) {
    userService.saveUser(request);
  }

  @PostMapping("/login")
  public AuthResponseDto login(@RequestBody AuthRequestDto request) {
    TokenDetails authenticate =
        securityService.authenticate(request.getUsername(), request.getPassword());

    return AuthResponseDto.builder()
        .userId(authenticate.getUserId())
        .token(authenticate.getToken())
        .issuedAt(authenticate.getIssuedAt())
        .expiredAt(authenticate.getExpiresAt())
        .build();
  }
}
