package com.example.beteyared.controller;

import com.example.beteyared.dto.LoginRequest;
import com.example.beteyared.model.Admin;
import com.example.beteyared.security.JwtTokenProvider;
import com.example.beteyared.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AdminService adminService,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider) {
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            log.debug("Login attempt for email: {}", loginRequest.getEmail());

            Admin admin = adminService.getAdminByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));

            log.debug("Comparing passwords for admin: {}", admin.getEmail());
            log.debug("Provided password hash: {}", passwordEncoder.encode(loginRequest.getPassword()));
            log.debug("Stored password hash: {}", admin.getPassword());

            if (!passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
                log.debug("Password mismatch");
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Invalid password"));
            }

            String token = jwtTokenProvider.createToken(admin.getEmail(), "ADMIN");

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "admin", Map.of(
                            "email", admin.getEmail(),
                            "firstName", admin.getFirstName(),
                            "lastName", admin.getLastName()
                    )
            ));

        } catch (Exception e) {
            log.error("Login error: ", e);
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid credentials"));
        }
    }
}