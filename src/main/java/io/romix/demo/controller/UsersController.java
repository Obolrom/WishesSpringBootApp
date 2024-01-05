package io.romix.demo.controller;

import io.romix.demo.controller.entity.GetAllUsersResponse;
import io.romix.demo.controller.entity.UserId;
import io.romix.demo.controller.entity.UserResponse;
import io.romix.demo.entity.UserEntity;
import io.romix.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<GetAllUsersResponse> getAllUsers() {
        final var result = new GetAllUsersResponse(userService.getAllUsers());

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") Long id) {
        return userService.findUserById(id)
                .map(wishEntity -> ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new UserResponse(wishEntity)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserId> createUser(@RequestBody UserEntity wish) {
        final var createdWish = userService.saveUser(wish);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new UserId(createdWish.getId()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") Long userId,
                                           @RequestBody UserEntity wish) {
        wish.setId(userId);
        userService.updateUser(wish);

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
