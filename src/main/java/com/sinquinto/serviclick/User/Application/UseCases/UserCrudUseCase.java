package com.sinquinto.serviclick.User.Application.UseCases;

import com.sinquinto.serviclick.User.Domain.User;
import com.sinquinto.serviclick.User.Infrastructure.DTO.UserDTO;

import java.util.List;

public interface UserCrudUseCase {
    User saveUser(User user);
    User findUserById(Long id);
    User findUserByEmail(String email);
    List<UserDTO> findAllUsers();
    User updateUser(Long id, User user);
    void deleteUserById(Long id);
}
