package com.fer.apuw.lab.tweetie.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fer.apuw.lab.tweetie.config.SecurityConfiguration;
import com.fer.apuw.lab.tweetie.post.dto.PostResponseDTO;
import com.fer.apuw.lab.tweetie.post.service.PostService;
import com.fer.apuw.lab.tweetie.user.dto.UserCreateDTO;
import com.fer.apuw.lab.tweetie.user.dto.UserPasswordDTO;
import com.fer.apuw.lab.tweetie.user.dto.UserRequestDTO;
import com.fer.apuw.lab.tweetie.user.dto.UserResponseDTO;
import com.fer.apuw.lab.tweetie.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfiguration.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    private Long adminId;
    private Long userId;

    @BeforeEach
    void setup() {
        adminId = createOrGetUser("admin", "admin@example.org", "password", true);
        userId = createOrGetUser("user", "user@example.org", "password", false);
    }

    // Helper metode
    private Long createOrGetUser(String username, String email, String password, boolean isAdmin) {
        if (!userService.existByUsername(username)) {
            UserCreateDTO user = new UserCreateDTO();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            if (isAdmin) userService.createAdmin(user);
            else userService.createUser(user);
        }
        return userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    private UserResponseDTO createUser(String username) {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername(username);
        dto.setEmail(username + "@example.org");
        dto.setPassword("password");
        return userService.createUser(dto);
    }

    // GET /api/users
    @Test
    void testGetAllUsers_asAdmin_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/users").with(httpBasic("admin", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetUserById_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/users/" + userId).with(httpBasic("admin", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId));
    }

    @Test
    void testGetUserById_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/users/999999").with(httpBasic("admin", "password")))
                .andExpect(status().isNotFound());
    }

    // GET /api/users/{id}/posts
    @Test
    void testGetUserPosts_shouldReturn200() throws Exception {
        List<PostResponseDTO> posts = postService.getPostsByUserId(userId);
        mockMvc.perform(get("/api/users/" + userId + "/posts").with(httpBasic("admin", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // POST /api/users
    @Test
    void testCreateUser_shouldReturn201() throws Exception {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername("newuser");
        dto.setEmail("newuser@example.org");
        dto.setPassword("password");

        mockMvc.perform(post("/api/users")
                        .with(httpBasic("admin", "password"))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("newuser"));
    }

    // PUT /api/users/{id}
    @Test
    void testUpdateUser_shouldReturn200() throws Exception {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setUsername("updateduser");
        dto.setEmail("updated@example.org");

        mockMvc.perform(put("/api/users/" + userId)
                        .with(httpBasic("admin", "password"))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updateduser"));
    }

    @Test
    void testUpdateUser_notFound_shouldReturn404() throws Exception {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setUsername("doesntExist");
        mockMvc.perform(put("/api/users/999999")
                        .with(httpBasic("admin", "password"))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    // PUT /api/users/{id}/password
    @Test
    void testUpdateUserPassword_shouldReturn200() throws Exception {
        UserPasswordDTO dto = new UserPasswordDTO();
        dto.setPassword("newPassword");

        mockMvc.perform(put("/api/users/" + userId + "/password")
                        .with(httpBasic("admin", "password"))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    // DELETE /api/users/{id}
    @Test
    void testDeleteUser_shouldReturn204() throws Exception {
        UserResponseDTO newUser = createUser("toDelete");

        mockMvc.perform(delete("/api/users/" + newUser.getId())
                        .with(httpBasic("admin", "password")))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUser_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/users/999999")
                        .with(httpBasic("admin", "password")))
                .andExpect(status().isNotFound());
    }

}
