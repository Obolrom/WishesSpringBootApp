package io.romix.demo.service;

import io.romix.demo.CustomException;
import io.romix.demo.controller.UserCreateRequest;
import io.romix.demo.controller.entity.AllUsersResponse;
import io.romix.demo.entity.ExpenseEntity;
import io.romix.demo.entity.Role;
import io.romix.demo.entity.UserEntity;
import io.romix.demo.mapper.UserMapper;
import io.romix.demo.repository.ExpenseRepository;
import io.romix.demo.repository.UserRepository;
import io.romix.demo.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AllUsersResponse getAllUsers() {
        List<UserResponse> usersResponse = userRepository.findAll().stream()
            .map(userMapper::toUser)
            .toList();

        return new AllUsersResponse(usersResponse);
    }

    @Transactional
    public UserResponse saveUser(UserCreateRequest userCreateRequest) {
        UserEntity user = UserEntity.builder()
            .username(userCreateRequest.getUsername())
            .password(passwordEncoder.encode(userCreateRequest.getPassword()))
            .role(Role.USER)
            .dateOfBirth(new Date())
            .photoUrl("")
            .expenses(new ArrayList<>())
            .build();

        return userMapper.toUser(userRepository.save(user));
    }

    @Transactional
    public Optional<UserResponse> findUserById(Long id) {
        return userRepository.findById(id)
            .map(userMapper::toUser);
    }

    public UserResponse getUserResponseByIdOrError(Long id) {
        return userRepository.findById(id)
            .map(userMapper::toUser)
            .orElseThrow(() ->
                new CustomException(
                    String.format("User with id %d not found", id),
                    HttpStatus.NOT_FOUND));
    }

    public UserEntity getUserByIdOrError(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() ->
                new CustomException(
                    String.format("User with id %d not found", id),
                    HttpStatus.NOT_FOUND));
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
