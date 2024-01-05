package io.romix.demo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Expense {
    private Long id;
    private Double expenseSum;
    private String category;
    private Date timestamp;
    private String description;
}
