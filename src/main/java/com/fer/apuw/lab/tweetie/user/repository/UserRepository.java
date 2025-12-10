package com.fer.apuw.lab.tweetie.user.repository;

import com.fer.apuw.lab.tweetie.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository {
    
    Optional<User> getUserById(long id);

    Optional<User> getUserByUsername(String username);

    Optional<User> getUserByEmail(String email);

    User createUser(User user);

    void updateUser(User user);

    void deleteUser(User user);
}
