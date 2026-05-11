package com.sinquinto.serviclick.User.Application;

import com.sinquinto.serviclick.Exception.Domain.ResourceDuplicateException;
import com.sinquinto.serviclick.Exception.Domain.ResourceNotFoundException;
import com.sinquinto.serviclick.User.Application.UseCases.UserCountUseCase;
import com.sinquinto.serviclick.User.Application.UseCases.UserCrudUseCase;
import com.sinquinto.serviclick.User.Domain.User;
import com.sinquinto.serviclick.User.Domain.UserRepository;
import com.sinquinto.serviclick.User.Infrastructure.DTO.UserDTO;
import com.sinquinto.serviclick.User.Infrastructure.Mapper.UserDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
public class UserService implements UserCountUseCase, UserCrudUseCase {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final UserDTOMapper mapper;

    @Override
    public User saveUser(User user) {
        User userDB = repository.findByEmail(user.getEmail());
        if(userDB != null) {
            throw new ResourceDuplicateException("User already exists in the system");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    public User findUserById(Long id) {
        User user = repository.findById(id);
        if(user == null){
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        User userDB = repository.findByEmail(email);
        if(userDB == null) {
            throw new ResourceNotFoundException("User with email " + email + " not found");
        }
        return userDB;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDTO> findAllUsers() {
        return repository.findAll()
                .stream()
                .map(mapper::userToUserDTO)
                .toList();
    }

    @Override
    public User updateUser(Long id, User user) {
        User UserDB = repository.findById(id);
        UserDB.setName(user.getName());
        UserDB.setLastName(user.getLastName());
        UserDB.setEmail(user.getEmail());
        UserDB.setPhoneNumber(user.getPhoneNumber());
        UserDB.setRole(user.getRole());
        if(user.getPassword() != null && !user.getPassword().isEmpty()){
            UserDB.setPassword(encoder.encode(user.getPassword()));
        }
        return repository.save(UserDB);
    }

    @Override
    public void deleteUserById(Long id) {
        User UserDB = repository.findById(id);
        if(UserDB == null){
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    @Override
    public Long countUsers() {
        return repository.countUsers();
    }
}
