package io.romix.demo.service;

import io.romix.demo.entity.WishEntity;
import io.romix.demo.repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishServiceImpl implements WishService {

    private final WishRepository wishRepository;

    @Autowired
    public WishServiceImpl(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    @Override
    public List<WishEntity> getAllWishes() {
        return wishRepository.findAll();
    }

    @Override
    public WishEntity saveWish(WishEntity wish) {
        return wishRepository.save(wish);
    }

    @Override
    public Optional<WishEntity> findWishById(Long id) {
        return wishRepository.findById(id);
    }

    @Override
    public void updateWish(WishEntity wish) {
        wishRepository.save(wish);
    }
}
