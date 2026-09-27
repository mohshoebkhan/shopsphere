package com.shopsphere.user.service;

import com.shopsphere.user.dto.UserResponse;
import com.shopsphere.user.entity.User;

import java.util.List;

public interface UserService {

    User register(User user);

    List<UserResponse> getAllUsers();
}