package com.fer.apuw.lab.tweetie.user.dto;

import com.fer.apuw.lab.tweetie.user.enums.RoleType;

import java.time.LocalDateTime;

public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private RoleType roleType;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
