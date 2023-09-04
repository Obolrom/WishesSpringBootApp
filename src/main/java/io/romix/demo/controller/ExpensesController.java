package io.romix.demo.controller;

import io.romix.demo.response.Expense;
import io.romix.demo.service.ExpensesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpensesController {
  private final ExpensesService expensesService;

  @GetMapping("/{user_id}")
  public ResponseEntity<List<Expense>> getAllExpensesForUser(@PathVariable("user_id") Long userId) {
    return ResponseEntity.ok(expensesService.getAllExpensesByUserId(userId));
  }

  @PostMapping("/{user_id}")
  public ResponseEntity<?> createNewExpenseForUser(
      @PathVariable("user_id") Long userId,
      @RequestBody Expense expense) {
    return expensesService.createExpenseForUser(userId, expense)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}