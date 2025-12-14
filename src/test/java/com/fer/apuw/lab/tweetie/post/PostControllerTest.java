package com.fer.apuw.lab.tweetie.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fer.apuw.lab.tweetie.config.SecurityConfiguration;
import com.fer.apuw.lab.tweetie.post.dto.PostCreateDTO;
import com.fer.apuw.lab.tweetie.post.dto.PostResponseDTO;
import com.fer.apuw.lab.tweetie.post.service.PostService;
import com.fer.apuw.lab.tweetie.user.dto.UserCreateDTO;
import com.fer.apuw.lab.tweetie.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfiguration.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @BeforeEach
    void setup() {
        createOrGetUser("admin", "admin@example.org", "password", true);
        createOrGetUser("user", "user@example.org", "password", false);
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

    private PostResponseDTO createPost(Long userId, String title, String content) {
        PostCreateDTO dto = new PostCreateDTO();
        dto.setUserId(userId);
        dto.setTitle(title);
        dto.setContent(content);
        return postService.createPost(dto);
    }

    // GET /api/posts
    @Test
    void testGetAllPosts_asUser_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/posts").with(httpBasic("user", "password")))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllPosts_asAdmin_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/posts").with(httpBasic("admin", "password")))
                .andExpect(status().isOk());
    }

    // POST /api/posts
    @Test
    void testCreatePost_asAdmin_shouldReturn201() throws Exception {
        Long adminId = createOrGetUser("admin", "admin@example.org", "password", true);
        PostCreateDTO dto = new PostCreateDTO();
        dto.setUserId(adminId);
        dto.setTitle("Admin Post");
        dto.setContent("Content");
        mockMvc.perform(post("/api/posts")
                        .with(httpBasic("admin", "password"))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Admin Post"));
    }

    @Test
    void testCreatePost_asUser_shouldReturn201() throws Exception {
        Long userId = createOrGetUser("user", "user@example.org", "password", false);
        PostCreateDTO dto = new PostCreateDTO();
        dto.setUserId(userId);
        dto.setTitle("User Post");
        dto.setContent("Content");
        mockMvc.perform(post("/api/posts")
                        .with(httpBasic("user", "password"))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("User Post"));
    }

    // GET /api/posts/{id}
    @Test
    void testGetPost_asAdmin_shouldReturn200() throws Exception {
        Long userId = createOrGetUser("user", "user@example.org", "password", false);
        PostResponseDTO post = createPost(userId, "Post", "Content");
        mockMvc.perform(get("/api/posts/" + post.getId()).with(httpBasic("admin", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Post"));
    }

    @Test
    void testGetPost_asUser_shouldReturn200() throws Exception {
        Long userId = createOrGetUser("user", "user@example.org", "password", false);
        PostResponseDTO post = createPost(userId, "Post", "Content");
        mockMvc.perform(get("/api/posts/" + post.getId()).with(httpBasic("user", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Post"));
    }

    // PUT /api/posts/{id}
    @Test
    void testUpdatePost_asAdmin_shouldReturn200() throws Exception {
        Long userId = createOrGetUser("user", "user@example.org", "password", false);
        PostResponseDTO post = createPost(userId, "Old Title", "Old Content");

        PostCreateDTO updateDto = new PostCreateDTO();
        updateDto.setUserId(userId);
        updateDto.setTitle("Updated Title");
        updateDto.setContent("Updated Content");

        mockMvc.perform(put("/api/posts/" + post.getId())
                        .with(httpBasic("admin", "password"))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void testUpdatePost_asUser_shouldReturn403() throws Exception {
        Long userId = createOrGetUser("user", "user@example.org", "password", false);
        PostResponseDTO post = createPost(userId, "Old Title", "Old Content");

        PostCreateDTO updateDto = new PostCreateDTO();
        updateDto.setUserId(userId);
        updateDto.setTitle("Updated Title");
        updateDto.setContent("Updated Content");

        mockMvc.perform(put("/api/posts/" + post.getId())
                        .with(httpBasic("user", "password"))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isForbidden());
    }

    // DELETE /api/posts/{id}
    @Test
    void testDeletePost_asAdmin_shouldReturn204() throws Exception {
        Long userId = createOrGetUser("user", "user@example.org", "password", false);
        PostResponseDTO post = createPost(userId, "Delete Me", "Content");

        mockMvc.perform(delete("/api/posts/" + post.getId())
                        .with(httpBasic("admin", "password")))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeletePost_asAdmin_shouldReturn404() throws Exception {
        Long userId = createOrGetUser("user", "user@example.org", "password", false);

        mockMvc.perform(delete("/api/posts/" + 999)
                        .with(httpBasic("admin", "password")))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletePost_asUser_shouldReturn403() throws Exception {
        Long userId = createOrGetUser("user", "user@example.org", "password", false);
        PostResponseDTO post = createPost(userId, "Delete Me", "Content");

        mockMvc.perform(delete("/api/posts/" + post.getId())
                        .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

}
