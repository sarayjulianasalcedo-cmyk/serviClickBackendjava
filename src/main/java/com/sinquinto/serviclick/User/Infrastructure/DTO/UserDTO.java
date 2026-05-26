package com.sinquinto.serviclick.User.Infrastructure.DTO;

import com.sinquinto.serviclick.User.Domain.Role;

public record UserDTO(Long userId, String name, String lastName, String email, String phoneNumber, Role role) {
}
