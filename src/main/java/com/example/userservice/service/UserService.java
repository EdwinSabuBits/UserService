package com.example.userservice.service;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User createUser(User user) {
        logger.info("Creating user with email: {}", user.getEmail());
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash the password before saving
            User savedUser = userRepository.save(user);
            logger.info("User created successfully with email: {}", user.getEmail());
            return savedUser;
        } catch (Exception e) {
            logger.error("Error creating user with email: {}", user.getEmail(), e);
            throw e;
        }
    }

    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        logger.info("Fetching user with id: {}", id);
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User user) {
        logger.info("Updating user with id: {}", id);
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(passwordEncoder.encode(user.getPassword())); // Hash the password before saving
            // Update other fields as necessary
            User updatedUser = userRepository.save(existingUser);
            logger.info("User updated successfully with id: {}", id);
            return updatedUser;
        } else {
            logger.error("User not found with id: {}", id);
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    public void deleteUser(Long id) {
        logger.info("Deleting user with id: {}", id);
        userRepository.deleteById(id);
    }
}
