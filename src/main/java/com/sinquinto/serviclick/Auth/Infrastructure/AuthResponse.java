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
    Long userId;
    String email;
    String name;
    String lastName;
    String phoneNumber;
    Role role;
    String token;
}