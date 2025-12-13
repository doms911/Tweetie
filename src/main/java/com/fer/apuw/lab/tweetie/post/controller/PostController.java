package com.fer.apuw.lab.tweetie.post.controller;

import com.fer.apuw.lab.tweetie.post.dto.PostCreateDTO;
import com.fer.apuw.lab.tweetie.post.dto.PostResponseDTO;
import com.fer.apuw.lab.tweetie.post.dto.PostUpdateDTO;
import com.fer.apuw.lab.tweetie.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Posts", description = "Post management")
@Controller
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @Operation(summary = "Get all posts", description = "Retrieve a list of all posts")
    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @Operation(summary = "Get a post by ID", description = "Retrieve a single post by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable Long id) {
        return postService.getPost(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new post", description = "Create a new post for an author with a title and content")
    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@Valid @RequestBody PostCreateDTO postCreateDTO) {
        return ResponseEntity.status(201).body(postService.createPost(postCreateDTO));
    }

    @Operation(summary = "Update an existing post", description = "Update a posts's title or content")
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long id,
                                                      @Valid @RequestBody PostUpdateDTO postUpdateDTO) {
        return postService.updatePost(id, postUpdateDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a post", description = "Soft delete a post by setting the deletedAt timestamp")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        boolean deleted = postService.deletePost(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
