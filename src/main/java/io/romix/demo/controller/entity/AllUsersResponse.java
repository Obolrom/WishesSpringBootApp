package io.romix.demo.controller.entity;

import io.romix.demo.response.UserResponse;
import lombok.*;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AllUsersResponse {

  private List<UserResponse> users;
}
