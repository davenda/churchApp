package com.example.beteyared.controller;

import com.example.beteyared.dto.AttendanceReport;
import com.example.beteyared.model.User;
import com.example.beteyared.service.AttendanceService;
import com.example.beteyared.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final AttendanceService attendanceService;

    @Autowired
    public UserController(UserService userService, AttendanceService attendanceService) {
        this.userService = userService;
        this.attendanceService = attendanceService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        try {
            // Check if email already exists
            if (userService.existsByEmail(user.getEmail())) {
                return ResponseEntity.badRequest()
                        .body("Email already registered");
            }

            User savedUser = userService.saveUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Registration failed: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            List<Map<String, Object>> usersWithAttendance = users.stream()
                    .map(user -> {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("id", user.getId());
                        userMap.put("firstName", user.getFirstName());
                        userMap.put("lastName", user.getLastName());
                        userMap.put("baptismName", user.getBaptismName());
                        userMap.put("email", user.getEmail());
                        userMap.put("phoneNumber", user.getPhoneNumber());
                        userMap.put("churchName", user.getChurchName());
                        userMap.put("country", user.getCountry());
                        userMap.put("state", user.getState());
                        userMap.put("city", user.getCity());
                        userMap.put("cohort", user.getCohort());

                        // Calculate attendance percentage using the simplified method
                        Double attendancePercentage = attendanceService.calculateUserAttendancePercentage(user.getId());
                        userMap.put("attendancePercentage", attendancePercentage);

                        return userMap;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(usersWithAttendance);
        } catch (Exception e) {
            log.error("Error getting users with attendance: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        try {
            User existingUser = userService.getUserById(id);
            if (existingUser == null) {
                return ResponseEntity.notFound().build();
            }

            // Update the existing user fields
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setBaptismName(user.getBaptismName());
            existingUser.setState(user.getState());
            existingUser.setCountry(user.getCountry());
            existingUser.setCity(user.getCity());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            existingUser.setEmail(user.getEmail());
            existingUser.setChurchName(user.getChurchName());

            User updatedUser = userService.saveUser(existingUser);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            User existingUser = userService.getUserById(id);
            if (existingUser == null) {
                return ResponseEntity.notFound().build();
            }
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}