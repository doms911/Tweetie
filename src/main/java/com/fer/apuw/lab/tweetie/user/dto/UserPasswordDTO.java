package com.fer.apuw.lab.tweetie.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserPasswordDTO {

    @NotBlank
    @Size(min = 6)
    String password;
}
