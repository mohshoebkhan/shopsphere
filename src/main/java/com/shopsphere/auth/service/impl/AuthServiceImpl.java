package com.shopsphere.auth.service.impl;

import com.shopsphere.auth.dto.LoginResponse;
import com.shopsphere.auth.dto.UserResponse;
import com.shopsphere.auth.service.AuthService;
import com.shopsphere.auth.entity.RefreshToken;
import com.shopsphere.user.entity.User;
import com.shopsphere.user.repository.UserRepository;
import com.shopsphere.auth.security.JwtService;
import com.shopsphere.auth.service.RefreshTokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    public AuthServiceImpl(
            UserRepository repository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public LoginResponse login(
            String email,
            String password) {

        User user = repository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (!passwordEncoder.matches(
                password,
                user.getPassword())) {

            throw new RuntimeException("Invalid password");
        }

        // Access Token
        String accessToken =
                jwtService.generateToken(
                        user.getEmail());

        // Refresh Token
        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        UserResponse response =
                UserResponse.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .mobile(user.getMobile())
                        .role(user.getRole())
                        .build();

        return new LoginResponse(
                accessToken,
                refreshToken.getToken(),
                response
        );
    }

    @Override
    public LoginResponse refreshToken(
            String refreshToken) {

        RefreshToken token =
                refreshTokenService.findByToken(
                        refreshToken);

        refreshTokenService.verifyExpiration(token);

        User user = token.getUser();

        String newAccessToken =
                jwtService.generateToken(
                        user.getEmail());

        UserResponse response =
                UserResponse.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .mobile(user.getMobile())
                        .role(user.getRole())
                        .build();

        return new LoginResponse(
                newAccessToken,
                token.getToken(),
                response
        );
    }
}