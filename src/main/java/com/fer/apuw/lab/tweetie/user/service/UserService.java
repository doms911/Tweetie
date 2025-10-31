package com.fer.apuw.lab.tweetie.user.service;

import com.fer.apuw.lab.tweetie.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;


}
