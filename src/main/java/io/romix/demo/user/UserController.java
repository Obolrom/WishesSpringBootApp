package io.romix.demo.user;

import io.romix.demo.controller.entity.AllUsersResponse;
import io.romix.demo.response.UserResponse;
import io.romix.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping
  public AllUsersResponse getAllUsers() {
    return userService.getAllUsers();
  }

  @GetMapping("/{id}")
  public UserResponse getUserById(@PathVariable("id") Long id) {
    return userService.getUserResponseByIdOrError(id);
  }
}
