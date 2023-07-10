package io.romix.demo.service;

import io.romix.demo.entity.WishEntity;

import java.util.List;
import java.util.Optional;

public interface WishService {

    List<WishEntity> getAllWishes();

    WishEntity saveWish(WishEntity wish);

    Optional<WishEntity> findWishById(Long id);

    void updateWish(WishEntity wish);
}
