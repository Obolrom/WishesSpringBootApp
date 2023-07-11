package io.romix.demo.controller;

import io.romix.demo.controller.entity.GetAllUsersResponse;
import io.romix.demo.controller.entity.UserId;
import io.romix.demo.controller.entity.UserResponse;
import io.romix.demo.entity.UserEntity;
import io.romix.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService wishService;

    @Autowired
    public UsersController(UserService userService) {
        this.wishService = userService;
    }

    @GetMapping
    public ResponseEntity<GetAllUsersResponse> getAllWishes() {
        final var result = new GetAllUsersResponse(wishService.getAllUsers());

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getWish(@PathVariable("id") Long id) {
        return wishService.findUserById(id)
                .map(wishEntity -> ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new UserResponse(wishEntity)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserId> createWish(@RequestBody UserEntity wish) {
        final var createdWish = wishService.saveUser(wish);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new UserId(createdWish.getId()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateWish(@PathVariable("id") Long userId,
                                           @RequestBody UserEntity wish) {
        wish.setId(userId);
        wishService.updateUser(wish);

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWish(@PathVariable("id") Long id) {
        wishService.deleteUser(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
