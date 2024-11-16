package com.example.userservice.controller;

import com.example.userservice.model.AuthenticationRequest;
import com.example.userservice.model.AuthenticationResponse;
import com.example.userservice.service.AuthenticationService;
import com.example.userservice.service.BlacklistService;
import com.example.userservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BlacklistService blacklistService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            logger.info("Received authentication request for user: {}", authenticationRequest.getUsername());
            AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);
            logger.info("Authentication successful for user: {}", authenticationRequest.getUsername());
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            logger.error("Authentication failed for user: {}", authenticationRequest.getUsername(), e);
            return ResponseEntity.status(403).body("Authentication failed: Invalid username or password.");
        } catch (Exception e) {
            logger.error("An error occurred during authentication for user: {}", authenticationRequest.getUsername(), e);
            return ResponseEntity.status(500).body("An error occurred during authentication. Please try again later.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                String jwt = token.substring(7);
                blacklistService.blacklistToken(jwt);
                logger.info("Token blacklisted successfully");
                return ResponseEntity.ok("Logout successful.");
            } else {
                return ResponseEntity.status(400).body("Invalid token.");
            }
        } catch (Exception e) {
            logger.error("An error occurred during logout.", e);
            return ResponseEntity.status(500).body("An error occurred during logout. Please try again later.");
        }
    }
}
