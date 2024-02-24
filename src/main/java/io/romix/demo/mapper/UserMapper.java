package io.romix.demo.mapper;

import io.romix.demo.entity.UserEntity;
import io.romix.demo.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ExpenseMapper expenseMapper;

    public UserResponse toUser(UserEntity userEntity) {
        return UserResponse.builder()
                .id(userEntity.getId())
                .photoUrl(userEntity.getPhotoUrl())
                .dateOfBirth(userEntity.getDateOfBirth())
                .description(userEntity.getDescription())
                .expenses(userEntity.getExpenses().stream().map(expenseMapper::toExpense).toList())
                .username(userEntity.getUsername())
                .build();
    }
}
