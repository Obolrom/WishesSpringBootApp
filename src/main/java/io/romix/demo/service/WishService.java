package io.romix.demo.service;

import io.romix.demo.entity.WishEntity;

import java.util.List;

public interface WishService {

    List<WishEntity> getAllWishes();

    WishEntity saveWish(WishEntity wish);

    WishEntity getWishById(Long id);

    void updateWish(WishEntity wish);
}
