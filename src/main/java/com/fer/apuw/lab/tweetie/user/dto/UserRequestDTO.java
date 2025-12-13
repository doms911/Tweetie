package com.fer.apuw.lab.tweetie.user.dto;

import com.fer.apuw.lab.tweetie.user.enums.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    @Size(max = 64)
    private String username;

    @Email
    private String email;
}
