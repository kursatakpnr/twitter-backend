package com.twitter.api.service.impl;

import com.twitter.api.dto.request.auth.LoginRequest;
import com.twitter.api.dto.request.auth.RegisterRequest;
import com.twitter.api.dto.response.auth.AuthResponse;
import com.twitter.api.entity.Role;
import com.twitter.api.entity.User;
import com.twitter.api.exception.ConflictException;
import com.twitter.api.exception.UnauthorizedException;
import com.twitter.api.repository.UserRepository;
import com.twitter.api.security.jwt.JwtTokenProvider;
import com.twitter.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email is already registered");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .displayName(request.displayName())
                .bio(request.bio())
                .profileImageUrl(request.profileImageUrl())
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        String token = jwtTokenProvider.generateToken(savedUser);
        return toAuthResponse(savedUser, token);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.usernameOrEmail(),
                            request.password()
                    )
            );
        } catch (AuthenticationException ex) {
            throw new UnauthorizedException("Invalid username/email or password");
        }

        User user = userRepository.findByUsernameOrEmail(request.usernameOrEmail(), request.usernameOrEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid username/email or password"));

        String token = jwtTokenProvider.generateToken(user);
        return toAuthResponse(user, token);
    }

    private AuthResponse toAuthResponse(User user, String token) {
        return new AuthResponse(
                token,
                "Bearer",
                jwtTokenProvider.getJwtExpirationMs(),
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                user.getRole()
        );
    }
}
