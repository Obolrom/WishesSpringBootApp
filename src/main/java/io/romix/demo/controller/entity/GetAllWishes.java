package io.romix.demo.controller.entity;

import io.romix.demo.entity.WishEntity;

import java.util.List;

public record GetAllWishes(
        List<WishEntity> wishes
) { }
