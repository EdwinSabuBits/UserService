package com.example.userservice.controller;

import com.example.userservice.dto.AuthStatusRequest;
import com.example.userservice.model.User;
import com.example.userservice.service.OrderService;
import com.example.userservice.service.UserService;
import com.example.userservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

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
        User user = userService.getUserById(id, loggedInEmail).orElse(null);
        if (user != null) {
            user.setOrders(orderService.getOrdersByUserId(id));
        }
        return user;
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

    @GetMapping("/authstatus")
    public Map<String, Boolean> checkAuthStatus(@RequestBody AuthStatusRequest authStatusRequest) {
        String token = authStatusRequest.getToken();
        Long userId = authStatusRequest.getUserId();
        boolean isValid = jwtUtil.validateToken(token, userService.loadUserById(userId));
        Map<String, Boolean> response = new HashMap<>();
        response.put("authStatus", isValid);
        return response;
    }

    private String getLoggedInEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
