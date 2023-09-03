package io.romix.demo.controller;

import io.romix.demo.controller.dto.ExpenseDto;
import io.romix.demo.controller.entity.GetAllUsersResponse;
import io.romix.demo.controller.entity.UserId;
import io.romix.demo.controller.entity.UserResponse;
import io.romix.demo.entity.ExpenseEntity;
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
    public ResponseEntity<GetAllUsersResponse> getAllWishes() {
        final var result = new GetAllUsersResponse(userService.getAllUsers());

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getWish(@PathVariable("id") Long id) {
        return userService.findUserById(id)
                .map(wishEntity -> ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new UserResponse(wishEntity)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserId> createWish(@RequestBody UserEntity wish) {
        final var createdWish = userService.saveUser(wish);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new UserId(createdWish.getId()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateWish(@PathVariable("id") Long userId,
                                           @RequestBody UserEntity wish) {
        wish.setId(userId);
        userService.updateUser(wish);

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWish(@PathVariable("id") Long id) {
        userService.deleteUser(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping("/{id}/expense")
    public ResponseEntity<Void> createExpense(
            @PathVariable("id") Long userId,
            @RequestBody ExpenseDto expense) {

        log.info("Create expense for user: {}, expense: {}", userId, expense);
        ExpenseEntity expenseEntity = ExpenseEntity.builder()
                .expenseSum(expense.getExpenseSum())
                .category(expense.getCategory())
                .build();
        userService.saveExpense(expenseEntity);

        return ResponseEntity.noContent()
                .build();
    }
}
