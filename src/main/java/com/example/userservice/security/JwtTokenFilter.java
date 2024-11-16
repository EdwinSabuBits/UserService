package com.example.userservice.security;

import com.example.userservice.service.BlacklistService;
import com.example.userservice.service.CustomUserDetailsService;
import com.example.userservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private BlacklistService blacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        logger.info("Entering JwtTokenFilter for URI: {}", request.getRequestURI());

        final String path = request.getRequestURI();
        if ("/authenticate".equals(path)) {
            chain.doFilter(request, response);
            logger.info("Exiting JwtTokenFilter for URI: {}", path);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");
        logger.info("Authorization Header: {}", authorizationHeader);

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
            logger.info("Extracted JWT: {}", jwt);
            logger.info("Extracted Username from JWT: {}", username);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("Username is not null and SecurityContext is empty");
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            if (!blacklistService.isTokenBlacklisted(jwt) && jwtUtil.validateToken(jwt, userDetails)) {
                logger.info("JWT token is valid");
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                logger.info("JWT token is validated and SecurityContext is set");
            } else {
                logger.info("JWT token is invalid or blacklisted");
            }
        }

        chain.doFilter(request, response);
        logger.info("Exiting JwtTokenFilter for URI: {}", request.getRequestURI());
    }
}
