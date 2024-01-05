package io.romix.demo.entity;

import java.util.Set;

public enum Role {
  ADMIN(Set.of(Permission.READ, Permission.WRITE)),
  USER(Set.of(Permission.READ));

  private final Set<Permission> permissions;

  Role(Set<Permission> permissions) {
    this.permissions = permissions;
  }
}
