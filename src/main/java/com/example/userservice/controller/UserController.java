package com.example.userservice.controller;

import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        String loggedInEmail = getLoggedInEmail();
        return userService.getUserById(id, loggedInEmail).orElse(null);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        String loggedInEmail = getLoggedInEmail();
        return userService.updateUser(id, user, loggedInEmail);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        String loggedInEmail = getLoggedInEmail();
        userService.deleteUser(id, loggedInEmail);
    }

    private String getLoggedInEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();  // Assuming the email is set as the principal (username)
    }
}
