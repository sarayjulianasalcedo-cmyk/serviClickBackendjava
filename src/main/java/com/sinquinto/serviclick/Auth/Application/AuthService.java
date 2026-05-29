package com.sinquinto.serviclick.Auth.Application;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.sinquinto.serviclick.Auth.Infrastructure.AuthResponse;
import com.sinquinto.serviclick.Auth.Infrastructure.GoogleLoginRequest;
import com.sinquinto.serviclick.Auth.Infrastructure.GooglePendingUserResponse;
import com.sinquinto.serviclick.Auth.Infrastructure.GoogleRegisterRequest;
import com.sinquinto.serviclick.Auth.Infrastructure.LoginRequest;
import com.sinquinto.serviclick.Auth.Infrastructure.RegisterRequest;
import com.sinquinto.serviclick.Config.Application.JwtService;
import com.sinquinto.serviclick.User.Application.UserService;
import com.sinquinto.serviclick.User.Domain.Role;
import com.sinquinto.serviclick.User.Domain.User;
import com.sinquinto.serviclick.User.Infrastructure.Mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements LoginUserCase {

    private final UserService userService;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    @Value("${google.client.id}")
    private String googleClientId;

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
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
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
                .token(jwtService.getToken(userMapper.userToUserEntity(userDB)))
                .userId(userDB.getUserId())
                .email(userDB.getEmail())
                .name(userDB.getName())
                .lastName(userDB.getLastName())
                .phoneNumber(userDB.getPhoneNumber())
                .role(userDB.getRole())
                .build();
    }

    public Object googleLogin(GoogleLoginRequest request) {
        Payload payload = verifyGoogleToken(request.getIdToken());

        String email = payload.getEmail();
        String name = (String) payload.getOrDefault("given_name", "Usuario");
        String lastName = (String) payload.getOrDefault("family_name", "Google");

        try {
            User user = userService.findUserByEmail(email);
            UserDetails userDetails = userMapper.userToUserEntity(user);
            String token = jwtService.getToken(userDetails);
            return AuthResponse.builder()
                    .token(token)
                    .email(user.getEmail())
                    .userId(user.getUserId())
                    .role(user.getRole())
                    .build();
        } catch (Exception e) {
            return GooglePendingUserResponse.builder()
                    .email(email)
                    .name(name)
                    .lastName(lastName)
                    .requiresRoleSelection(true)
                    .build();
        }
    }

    public AuthResponse googleRegister(GoogleRegisterRequest request) {
        Payload payload = verifyGoogleToken(request.getIdToken());

        String email = payload.getEmail();
        String tokenName = (String) payload.getOrDefault("given_name", "Usuario");
        String tokenLastName = (String) payload.getOrDefault("family_name", "Google");

        String name = (request.getName() != null && !request.getName().isBlank())
                ? request.getName() : tokenName;
        String lastName = (request.getLastName() != null && !request.getLastName().isBlank())
                ? request.getLastName() : tokenLastName;

        User nuevoUsuario = User.builder()
                .email(email)
                .name(name)
                .lastName(lastName)
                .phoneNumber(request.getPhoneNumber())
                .password(UUID.randomUUID().toString())
                .role(request.getRole())
                .registerDate(LocalDateTime.now())
                .build();

        User user = userService.saveUser(nuevoUsuario);
        UserDetails userDetails = userMapper.userToUserEntity(user);
        String token = jwtService.getToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();
    }

    protected Payload verifyGoogleToken(String rawToken) {
        log.debug("[Google] Verificando token contra audience configurado");

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(rawToken);
        } catch (Exception e) {
            log.error("[Google] Error al verificar token: {}", e.getMessage(), e);
            throw new RuntimeException("Token de Google inválido o expirado: " + e.getMessage());
        }

        if (idToken == null) {
            throw new RuntimeException("Token de Google no pudo verificarse (idToken null — audience mismatch o token expirado)");
        }

        return idToken.getPayload();
    }
}