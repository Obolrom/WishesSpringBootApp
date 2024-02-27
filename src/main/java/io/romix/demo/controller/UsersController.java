package io.romix.demo.controller;

import io.romix.demo.controller.entity.AllUsersResponse;
import io.romix.demo.controller.entity.UserResponseOld;
import io.romix.demo.entity.UserEntity;
import io.romix.demo.response.UserResponse;
import io.romix.demo.service.UserService;
import io.romix.demo.websocket.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping
    public ResponseEntity<AllUsersResponse> getAllUsers(Principal principal) {
        log.info("Principal: {}", principal);
        final var usersResponse = userService.getAllUsers();

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(usersResponse);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
        @RequestBody @Validated UserCreateRequest userCreateRequest) {
        final var user = userService.saveUser(userCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(user);
    }

    @PostMapping("/messages")
    public void sendMessage() {
        simpMessagingTemplate.convertAndSend(
            "/topic/greetings",
            new MessageDto("Hello from UsersController!"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseOld> getUser(@PathVariable("id") Long id) {
        return userService.findUserById(id)
                .map(wishEntity -> ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new UserResponseOld(wishEntity)))
                .orElse(ResponseEntity.notFound().build());
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
