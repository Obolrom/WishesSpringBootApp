package io.romix.demo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String photoUrl;
    private String description;
    private Date dateOfBirth;
    private List<Expense> expenses;
}
