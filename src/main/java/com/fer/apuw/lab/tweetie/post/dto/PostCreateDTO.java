package com.fer.apuw.lab.tweetie.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostCreateDTO {
    @NotNull
    private Long userId;

    @NotBlank
    @Size(max = 200)
    private String title;

    @NotBlank
    private String content;
}
