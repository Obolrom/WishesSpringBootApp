package io.romix.demo.service;

import io.romix.demo.entity.CategoryEntity;
import io.romix.demo.entity.ExpenseEntity;
import io.romix.demo.entity.UserEntity;
import io.romix.demo.mapper.ExpenseMapper;
import io.romix.demo.repository.CategoryRepository;
import io.romix.demo.repository.ExpenseRepository;
import io.romix.demo.repository.UserRepository;
import io.romix.demo.response.Expense;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpensesService {
  private final ExpenseRepository expenseRepository;
  private final CategoryRepository categoryRepository;
  private final UserRepository userRepository;
  private final ExpenseMapper expenseMapper;

  public List<Expense> getAllExpensesByUserId(Long userId) {
    return userRepository.findById(userId)
        .map(UserEntity::getExpenses)
        .map(expenseEntities -> expenseEntities.stream()
            .map(expenseMapper::toExpense)
            .toList()
        )
        .orElse(List.of());
  }

  public Optional<Expense> createExpenseForUser(Long userId, Expense expense) {
    CategoryEntity categoryEntity = categoryRepository.findByName(expense.getCategory())
        .orElseGet(() -> categoryRepository.save(CategoryEntity.builder().name(expense.getCategory()).build()));

    ExpenseEntity expenseEntity = ExpenseEntity.builder()
        .description(expense.getDescription())
        .expenseSum(expense.getExpenseSum())
        .categoryEntity(categoryEntity)
        .timestamp(expense.getTimestamp())
        .build();

    return userRepository.findById(userId)
        .map(user -> {
          expenseEntity.setUser(user);
          return expenseEntity;
        })
        .map(expenseRepository::save)
        .map(expenseMapper::toExpense);
  }

  public void deleteExpense(Long userId, Long expenseId) {
    expenseRepository.findById(expenseId)
        .filter(expense -> expense.getUser().getId().equals(userId))
        .ifPresent(expenseRepository::delete);
  }
}
