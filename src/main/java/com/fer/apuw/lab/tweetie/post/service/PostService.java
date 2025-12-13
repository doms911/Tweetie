package com.fer.apuw.lab.tweetie.post.service;

import com.fer.apuw.lab.tweetie.post.dto.PostCreateDTO;
import com.fer.apuw.lab.tweetie.post.dto.PostResponseDTO;
import com.fer.apuw.lab.tweetie.post.dto.PostUpdateDTO;
import com.fer.apuw.lab.tweetie.post.mapper.PostMapper;
import com.fer.apuw.lab.tweetie.post.model.Post;
import com.fer.apuw.lab.tweetie.post.repository.PostRepository;
import com.fer.apuw.lab.tweetie.user.model.User;
import com.fer.apuw.lab.tweetie.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    public PostResponseDTO createPost(PostCreateDTO postCreateDTO) {
        Long userId = postCreateDTO.getUserId();
        if (userId == null || userRepository.getUserById(userId).isEmpty()) {
            throw new IllegalArgumentException("User does not exist");
        }
        return postMapper.toResponse(postRepository.createPost(postMapper.toModel(postCreateDTO)));
    }

    public Optional<PostResponseDTO> getPost(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        Optional<Post> post = postRepository.getPost(id);
        if (post.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(postMapper.toResponse(post.get()));
    }

    public List<PostResponseDTO> getAllPosts() {
        return postMapper.toResponse(postRepository.getAllPosts());
    }

    public List<PostResponseDTO> getPostsByUserId(Long userId) {
        if (userId == null) {
            return List.of();
        }
        Optional<User> user = userRepository.getUserById(userId);
        if (user.isEmpty()) {
            return List.of();
        }
        List<Post> posts = postRepository.getPostsByUserId(userId);
        return postMapper.toResponse(posts);
    }

    public Optional<PostResponseDTO> updatePost(Long id, PostUpdateDTO postUpdateDTO) {
        if (postUpdateDTO == null || id  == null) {
            return Optional.empty();
        }
        Optional<Post> post = postRepository.getPost(id);
        if (post.isEmpty()) {
            return Optional.empty();
        }
        Post postEntity = post.get();
        postMapper.updateFromDto(postUpdateDTO, postEntity);

        return postRepository.updatePost(postEntity)
                .map(postMapper::toResponse);
    }

    public boolean deletePost(Long id) {
        if (id == null) {
            return false;
        }
        return postRepository.softDeletePostById(id);
    }
}
