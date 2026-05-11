package com.sinquinto.serviclick.User.Infrastructure.Repository;

import com.sinquinto.serviclick.User.Domain.User;
import com.sinquinto.serviclick.User.Domain.UserRepository;
import com.sinquinto.serviclick.User.Infrastructure.Entity.UserEntity;
import com.sinquinto.serviclick.User.Infrastructure.Mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserMySqlRepository implements UserRepository {

    private final SpringUserRepository repository;
    private final UserMapper mapper;

    @Override
    public User save(User user) {
        UserEntity saved = repository.save(mapper.userToUserEntity(user));
        return mapper.userEntityToUser(saved);
    }

    @Override
    public User findByEmail(String email) {
        return mapper.userEntityToUser(repository.findByEmail(email));
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id).map(mapper::userEntityToUser).orElse(null);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream().map(mapper::userEntityToUser).toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Long countUsers() {
        return repository.count();
    }
}
