package com.example.auth.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Boolean enabled;
    private String roles;
    private String resetToken;
    private LocalDateTime resetTokenExpiresAt;
}
