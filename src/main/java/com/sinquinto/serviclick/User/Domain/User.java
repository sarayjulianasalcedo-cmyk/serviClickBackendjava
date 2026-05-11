package com.sinquinto.serviclick.User.Domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    Long userId;
    String name;
    String lastName;
    String email;
    String password;
    String phoneNumber;
    Role role;
    LocalDateTime registerDate;
}
