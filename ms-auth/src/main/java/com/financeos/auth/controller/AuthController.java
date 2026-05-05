package com.financeos.auth.controller;

import com.financeos.auth.dto.LoginDTO;
import com.financeos.auth.dto.RegisterDTO;
import com.financeos.auth.dto.TokenDTO;
import com.financeos.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<TokenDTO> register(
            @Valid @RequestBody RegisterDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(
            @Valid @RequestBody LoginDTO dto) {
        return ResponseEntity
                .ok(authService.login(dto));
    }
}