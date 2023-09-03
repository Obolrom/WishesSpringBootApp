package io.romix.demo.mapper;

import io.romix.demo.entity.ExpenseEntity;
import io.romix.demo.response.Expense;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpenseMapper {
    Expense toExpense(ExpenseEntity expenseEntity) {
        return Expense.builder()
                .id(expenseEntity.getId())
                .description(expenseEntity.getDescription())
                .expenseSum(expenseEntity.getExpenseSum())
                .category(expenseEntity.getCategory())
                .timestamp(expenseEntity.getTimestamp())
                .build();
    }
}
