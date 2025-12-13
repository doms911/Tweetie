package com.fer.apuw.lab.tweetie.post.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Post {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
