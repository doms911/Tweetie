package com.fer.apuw.lab.tweetie.user.repository;

import com.fer.apuw.lab.tweetie.user.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    
    Optional<User> getUserById(Long id);

    List<User> getAllUsers();

    Optional<User> getUserByUsername(String username);

    Optional<User> getUserByEmail(String email);

    boolean existByUsername(String username);

    boolean existByEmail(String email);

    User createUser(User user);

    Optional<User> updateUser(User user);

    Optional<User> updateUserPassword(User user);

    boolean softDeleteUserById(Long id);
}
