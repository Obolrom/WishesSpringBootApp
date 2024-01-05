package io.romix.demo.service;

import io.romix.demo.entity.ExpenseEntity;
import io.romix.demo.entity.UserEntity;
import io.romix.demo.mapper.UserMapper;
import io.romix.demo.repository.ExpenseRepository;
import io.romix.demo.repository.UserRepository;
import io.romix.demo.response.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final UserMapper userMapper;

    @Transactional
    public List<User> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();

        return userEntities.stream()
                .map(userMapper::toUser)
                .toList();
    }

    public UserEntity saveUser(UserEntity wish) {
        return userRepository.save(wish);
    }

    @Transactional
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUser);
    }

    public void updateUser(UserEntity wish) {
        userRepository.save(wish);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void saveExpense(ExpenseEntity expense) {
        expenseRepository.save(expense);
    }
}
