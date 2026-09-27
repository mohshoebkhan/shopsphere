package com.shopsphere.user.controller;

import com.shopsphere.user.dto.UserResponse;
import com.shopsphere.user.entity.User;
import com.shopsphere.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return service.register(user);
    }

    @GetMapping
    public List<UserResponse> getUsers() {
        return service.getAllUsers();
    }


}