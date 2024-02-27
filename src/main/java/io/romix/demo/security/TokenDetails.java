package io.romix.demo.security;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TokenDetails {
  private Long userId;
  private String token;
  private Date issuedAt;
  private Date expiresAt;
}
