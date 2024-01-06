package io.romix.demo.service;

import io.romix.demo.CustomException;
import io.romix.demo.entity.CategoryEntity;
import io.romix.demo.entity.ExpenseEntity;
import io.romix.demo.entity.UserEntity;
import io.romix.demo.expenses.ExpenseCreateRequest;
import io.romix.demo.mapper.ExpenseMapper;
import io.romix.demo.repository.CategoryRepository;
import io.romix.demo.repository.ExpenseRepository;
import io.romix.demo.repository.UserRepository;
import io.romix.demo.response.Expense;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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

  @Transactional
  public Optional<Expense> createExpenseForUser(Long userId, ExpenseCreateRequest expense) {
    CategoryEntity categoryEntity = categoryRepository
        .findById(expense.getCategoryId())
        .orElseThrow(() ->
            new CustomException(
                String.format("No category was found with id %d", expense.getCategoryId()),
                HttpStatus.NOT_FOUND));

    ExpenseEntity expenseEntity = ExpenseEntity.builder()
        .description(expense.getDescription())
        .expenseSum(expense.getExpenseSum())
        .categoryEntity(categoryEntity)
        .timestamp(new Date())
        .build();

    return userRepository.findById(userId)
        .map(user -> {
          expenseEntity.setUser(user);
          return expenseEntity;
        })
        .map(expenseRepository::save)
        .map(expenseMapper::toExpense);
  }

  @Transactional
  public void deleteExpense(Long expenseId) {
    ExpenseEntity expense = expenseRepository
        .findById(expenseId)
        .orElseThrow(() ->
            new CustomException(
                String.format("No expense was found with id %d", expenseId),
                HttpStatus.NOT_FOUND));

    expenseRepository.delete(expense);
  }
}
