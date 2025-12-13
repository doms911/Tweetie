package com.fer.apuw.lab.tweetie.user.model;

import com.fer.apuw.lab.tweetie.user.enums.RoleType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private RoleType roleType;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
