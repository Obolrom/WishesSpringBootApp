package io.romix.demo.security;

import lombok.*;

import java.security.Principal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomPrincipal implements Principal {
  private Long id;
  private String username;

  @Override
  public String getName() {
    return username;
  }
}
