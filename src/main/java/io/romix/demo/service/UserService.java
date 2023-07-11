package io.romix.demo.service;

import io.romix.demo.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserEntity> getAllUsers();

    UserEntity saveUser(UserEntity wish);

    Optional<UserEntity> findUserById(Long id);

    void updateUser(UserEntity wish);

    void deleteUser(Long id);
}
