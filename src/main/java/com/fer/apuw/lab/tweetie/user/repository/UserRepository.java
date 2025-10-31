package com.fer.apuw.lab.tweetie.user.repository;

import com.fer.apuw.lab.tweetie.user.model.User;
import org.springframework.stereotype.Repository;

public interface UserRepository {
    
    User getUserById(long id);
    
    User getUserByUsername(String username);
    
    User getUserByEmail(String email);

    User createUser(User user);

    void updateUser(User user);

    void deleteUser(User user);
}
