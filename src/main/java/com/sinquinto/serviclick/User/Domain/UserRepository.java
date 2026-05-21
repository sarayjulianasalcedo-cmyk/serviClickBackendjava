package com.sinquinto.serviclick.User.Domain;

import java.util.List;

public interface UserRepository {
    User save(User user);
    User findByEmail(String email);
    User findById(Long id);
    List<User> findAll();
    void deleteById(Long id);
    Long countUsers();
}