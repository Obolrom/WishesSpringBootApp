package io.romix.demo.expenses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseCreateRequest {
  private Double expenseSum;

  private Long categoryId;

  private String description;
}
