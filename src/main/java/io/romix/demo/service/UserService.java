package io.romix.demo.service;

import io.romix.demo.entity.ExpenseEntity;
import io.romix.demo.entity.UserEntity;
import io.romix.demo.mapper.UserMapper;
import io.romix.demo.repository.ExpenseRepository;
import io.romix.demo.repository.WishRepository;
import io.romix.demo.response.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final WishRepository wishRepository;
    private final ExpenseRepository expenseRepository;
    private final UserMapper userMapper;

    public List<User> getAllUsers() {
        List<UserEntity> userEntities = wishRepository.findAll();

        return userEntities.stream()
                .map(userMapper::toUser)
                .toList();
    }

    public UserEntity saveUser(UserEntity wish) {
        return wishRepository.save(wish);
    }

    public Optional<User> findUserById(Long id) {
        return wishRepository.findById(id)
                .map(userMapper::toUser);
    }

    public void updateUser(UserEntity wish) {
        wishRepository.save(wish);
    }

    public void deleteUser(Long id) {
        wishRepository.deleteById(id);
    }

    public void saveExpense(ExpenseEntity expense) {
        expenseRepository.save(expense);
    }
}
