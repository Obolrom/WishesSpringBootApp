package io.romix.demo.controller.entity;

import io.romix.demo.entity.UserEntity;

import java.util.List;

public record GetAllUsersResponse(
        List<UserEntity> wishes
) { }
