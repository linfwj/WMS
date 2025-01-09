package com.example.auth.service;

import com.example.auth.entity.User;
import com.example.auth.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
    
    public boolean initiatePasswordReset(String email) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            return false;
        }
        
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiresAt(LocalDateTime.now().plusHours(1));
        userMapper.updateResetToken(user);
        
        // TODO: Send password reset email with link containing token
        // Example: http://localhost:5173/reset-password?token={token}
        return true;
    }
    
    public boolean resetPassword(String token, String newPassword) {
        User user = userMapper.findByResetToken(token);
        if (user == null || user.getResetTokenExpiresAt() == null || 
            user.getResetTokenExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiresAt(null);
        userMapper.updatePassword(user);
        
        return true;
    }
}
