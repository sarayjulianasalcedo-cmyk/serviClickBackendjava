package com.sinquinto.serviclick.Auth.Application;

import com.sinquinto.serviclick.Auth.Infrastructure.AuthResponse;
import com.sinquinto.serviclick.Auth.Infrastructure.LoginRequest;
import com.sinquinto.serviclick.Auth.Infrastructure.RegisterRequest;
import com.sinquinto.serviclick.Config.Application.JwtService;
import com.sinquinto.serviclick.User.Application.UserService;
import com.sinquinto.serviclick.User.Domain.User;
import com.sinquinto.serviclick.User.Infrastructure.Mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService implements LoginUserCase {

    private final UserService userService;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userService.findUserByEmail(request.getEmail());
        UserDetails userDetails = userMapper.userToUserEntity(user);
        String token = jwtService.getToken(userDetails);
        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .userId(user.getUserId())
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthResponse register(RegisterRequest request){
        User user = User.builder()
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .name(request.getName())
                .lastName(request.getLastName())
                .password(request.getPassword())
                .role(request.getRole())
                .registerDate(LocalDateTime.now())
                .build();

        User userDB = userService.saveUser(user);
        return AuthResponse.builder()
                .token(jwtService.getToken(userMapper.userToUserEntity(user)))
                .email(userDB.getEmail())
                .userId(userDB.getUserId())
                .build();
    }
}