package com.sinquinto.serviclick.Auth.Infrastructure;

import com.sinquinto.serviclick.User.Domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    String name;
    String lastName;
    String email;
    String password;
    String phoneNumber;
    Role role;
    LocalDateTime registerDate;
}
