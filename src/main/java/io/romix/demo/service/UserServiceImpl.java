package io.romix.demo.service;

import io.romix.demo.entity.UserEntity;
import io.romix.demo.repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final WishRepository wishRepository;

    @Autowired
    public UserServiceImpl(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return wishRepository.findAll();
    }

    @Override
    public UserEntity saveUser(UserEntity wish) {
        return wishRepository.save(wish);
    }

    @Override
    public Optional<UserEntity> findUserById(Long id) {
        return wishRepository.findById(id);
    }

    @Override
    public void updateUser(UserEntity wish) {
        wishRepository.save(wish);
    }

    @Override
    public void deleteUser(Long id) {
        wishRepository.deleteById(id);
    }
}
