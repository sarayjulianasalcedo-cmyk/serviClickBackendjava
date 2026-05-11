package com.sinquinto.serviclick.Auth.Infrastructure;

import com.sinquinto.serviclick.User.Domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    String email;
    Long userId;
    String token;
    Role role;
}