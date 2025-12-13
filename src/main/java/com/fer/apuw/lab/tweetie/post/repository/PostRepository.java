package com.fer.apuw.lab.tweetie.post.repository;

import com.fer.apuw.lab.tweetie.post.model.Post;
import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post createPost(Post post);

    Optional<Post> getPost(Long id);

    List<Post> getAllPosts();

    List<Post> getPostsByUserId(Long userId);

    Optional<Post> updatePost(Post post);

    boolean softDeletePostById(Long id);
}
