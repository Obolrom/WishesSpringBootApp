package io.romix.demo.mapper;

import io.romix.demo.entity.UserEntity;
import io.romix.demo.response.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ExpenseMapper expenseMapper;

    public User toUser(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .photoUrl(userEntity.getPhotoUrl())
                .dateOfBirth(userEntity.getDateOfBirth())
                .description(userEntity.getDescription())
                .expenses(userEntity.getExpenses().stream().map(expenseMapper::toExpense).toList())
                .username(userEntity.getUsername())
                .build();
    }
}
