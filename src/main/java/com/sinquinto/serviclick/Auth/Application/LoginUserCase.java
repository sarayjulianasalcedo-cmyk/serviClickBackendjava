package com.sinquinto.serviclick.Auth.Application;

import com.sinquinto.serviclick.Auth.Infrastructure.AuthResponse;
import com.sinquinto.serviclick.Auth.Infrastructure.LoginRequest;
import com.sinquinto.serviclick.Auth.Infrastructure.RegisterRequest;

public interface LoginUserCase {
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
}