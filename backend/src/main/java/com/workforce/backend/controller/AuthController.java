package com.workforce.backend.controller;

import com.workforce.backend.dto.request.LoginRequestDTO;
import com.workforce.backend.dto.request.RegisterRequestDTO;
import com.workforce.backend.dto.response.AuthResponseDTO;
import com.workforce.backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO request) {
        authService.registerUser(request);
        return ResponseEntity.ok("Registrasi berhasil!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}