package com.twitter.api.service;

import com.twitter.api.dto.request.auth.LoginRequest;
import com.twitter.api.dto.request.auth.RegisterRequest;
import com.twitter.api.dto.response.auth.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
