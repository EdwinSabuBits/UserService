package com.example.userservice.service;

import com.example.userservice.model.AuthenticationRequest;
import com.example.userservice.model.AuthenticationResponse;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private Set<String> invalidatedTokens = new HashSet<>();

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        logger.info("Attempting to authenticate user with email: {}", authenticationRequest.getEmail());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
            logger.info("Authentication successful for user with email: {}", authenticationRequest.getEmail());

            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
            final String jwtToken = jwtUtil.generateToken(userDetails.getUsername());

            return new AuthenticationResponse(jwtToken);
        } catch (Exception e) {
            logger.error("Authentication failed for user with email: {}", authenticationRequest.getEmail(), e);
            throw e;
        }
    }

    public void logout(String token) {
        invalidatedTokens.add(token);
    }

    public boolean isTokenInvalidated(String token) {
        return invalidatedTokens.contains(token);
    }
}
