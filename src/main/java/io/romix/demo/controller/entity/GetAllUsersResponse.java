package io.romix.demo.controller.entity;

import io.romix.demo.response.User;

import java.util.List;

public record GetAllUsersResponse(
        List<User> wishes
) { }
