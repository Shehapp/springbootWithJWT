package com.example.springbootwithjwt.controller;

import com.example.springbootwithjwt.entity.RequestAuthenticate;
import com.example.springbootwithjwt.entity.RequestRegister;
import com.example.springbootwithjwt.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    final public AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RequestRegister requestRegister) {
        return ResponseEntity.ok(authService.register(requestRegister));

    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> login(@RequestBody RequestAuthenticate requestRegister) {
        return ResponseEntity.ok(authService.authenticate(requestRegister));
    }


}
