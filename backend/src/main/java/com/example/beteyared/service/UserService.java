package com.example.beteyared.service;

import com.example.beteyared.model.User;
import com.example.beteyared.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        log.info("Saving new user with email: {}", user.getEmail());
        return userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> getUsersByCohort(String cohort) {
        log.info("Getting active users for cohort: {}", cohort);
        return userRepository.findByCohortAndIsActiveTrue(cohort);
    }

    public List<User> getAllActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }
}
