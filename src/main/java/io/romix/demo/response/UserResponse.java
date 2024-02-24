package io.romix.demo.response;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String photoUrl;
    private String description;
    private Date dateOfBirth;
    private List<Expense> expenses;
}
