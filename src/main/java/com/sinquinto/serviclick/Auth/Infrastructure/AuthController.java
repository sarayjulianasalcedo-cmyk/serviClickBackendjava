package com.sinquinto.serviclick.Auth.Infrastructure;


import com.sinquinto.serviclick.Auth.Application.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/google-login")
    public ResponseEntity<Object> googleLogin(@RequestBody GoogleLoginRequest request) {
        System.out.println("[ServiClick] idToken recibido null? " + (request.getIdToken() == null));
        System.out.println("[ServiClick] idToken length = " + (request.getIdToken() != null ? request.getIdToken().length() : 0));

        Object result = authService.googleLogin(request);

        if (result instanceof GooglePendingUserResponse) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/google-register")
    public ResponseEntity<AuthResponse> googleRegister(@RequestBody GoogleRegisterRequest request) {
        System.out.println("[ServiClick] google-register idToken null? " + (request.getIdToken() == null));
        return ResponseEntity.ok(authService.googleRegister(request));
    }
}
