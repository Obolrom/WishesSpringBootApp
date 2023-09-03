package io.romix.demo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
public class Expense {
    private Long id;
    private Double expenseSum;
    private String category;
    private Date timestamp;
    private String description;
}
