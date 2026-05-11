package com.sinquinto.serviclick.User.Infrastructure;

import com.sinquinto.serviclick.User.Application.UserService;
import com.sinquinto.serviclick.User.Domain.User;
import com.sinquinto.serviclick.User.Infrastructure.DTO.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<User> save(@RequestBody User user) {
        User saved = service.saveUser(user);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        List<UserDTO> userList = service.findAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        User user = service.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("email/{email}")
    public ResponseEntity<User> findByEmail(@PathVariable String email) {
        User user = service.findUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countUsers() {
        Long count = service.countUsers();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> update(@RequestBody User user) {
        User userDB = service.updateUser(user.getUserId(), user);
        return new ResponseEntity<>(userDB, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteUserById(id);
    }
}
