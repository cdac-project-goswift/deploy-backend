package com.goswift.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        boolean validHeader = authHeader != null && authHeader.startsWith("Bearer ");
        Authentication auth = null;
        
        if (validHeader) {
            String token = authHeader.replace("Bearer ", "").trim();
            try {
                auth = jwtUtil.validateToken(token);
                System.out.println("JWT validated. Authorities: " + auth.getAuthorities());
            } catch (Exception e) {
                System.out.println("JWT validation failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        if (auth != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println("Auth set in SecurityContext");
        }
        
        filterChain.doFilter(request, response);
    }
}