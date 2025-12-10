package com.fer.apuw.lab.tweetie.user.service;

import com.fer.apuw.lab.tweetie.user.dto.User;
import com.fer.apuw.lab.tweetie.user.mapper.UserMapper;
import com.fer.apuw.lab.tweetie.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;

    public Optional<User> getUserById(long id) {
        return null;
    }

    public User getUserByEmail(String email) {}



}
