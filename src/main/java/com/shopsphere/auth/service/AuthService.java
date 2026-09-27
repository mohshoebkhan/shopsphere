package com.shopsphere.auth.service;


import com.shopsphere.auth.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(
            String email,
            String password);

    LoginResponse refreshToken(
            String refreshToken);
}