package io.romix.demo.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
  private Long userId;
  private String token;
  private Date issuedAt;
  private Date expiredAt;
}
