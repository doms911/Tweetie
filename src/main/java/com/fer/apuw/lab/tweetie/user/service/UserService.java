package com.fer.apuw.lab.tweetie.user.service;

import com.fer.apuw.lab.tweetie.user.dto.UserCreateDTO;
import com.fer.apuw.lab.tweetie.user.dto.UserPasswordDTO;
import com.fer.apuw.lab.tweetie.user.dto.UserRequestDTO;
import com.fer.apuw.lab.tweetie.user.dto.UserResponseDTO;
import com.fer.apuw.lab.tweetie.user.mapper.UserMapper;
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
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDTO createUser(UserCreateDTO userCreateDTO) {
        return userMapper.toResponse(userRepository.createUser(userMapper.toModel(userCreateDTO)));
    }

    public List<UserResponseDTO> getAllUsers() {
        return userMapper.toResponse(userRepository.getAllUsers());
    }

    public Optional<UserResponseDTO> getUserById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        Optional<User> user = userRepository.getUserById(id);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(userMapper.toResponse(user.get()));
    }

    public Optional<UserResponseDTO> getUserByUsername(String username) {
        if (username == null) {
            return Optional.empty();
        }
        Optional<User> user = userRepository.getUserByUsername(username);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(userMapper.toResponse(user.get()));
    }

    public Optional<UserResponseDTO> getUserByEmail(String email) {
        if  (email == null) {
            return Optional.empty();
        }
        Optional<User> user = userRepository.getUserByEmail(email);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(userMapper.toResponse(user.get()));
    }

    public boolean existByUsername(String username) {
        if (username == null) {
            return false;
        }
        return userRepository.existByUsername(username);
    }

    public boolean existByEmail(String email) {
        if (email == null) {
            return false;
        }
        return userRepository.existByEmail(email);
    }

    public Optional<UserResponseDTO> updateUser(Long id, UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null || id == null) {
            return Optional.empty();
        }
        Optional<User> user = userRepository.getUserById(id);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        User userEntity = user.get();
        userMapper.updateFromDto(userRequestDTO, userEntity);

        return userRepository.updateUser(userEntity)
                .map(userMapper::toResponse);
    }

    public Optional<UserResponseDTO> updateUserPassword(Long id, UserPasswordDTO userPasswordDTO) {
        if (userPasswordDTO == null || id == null) {
            return Optional.empty();
        }
        Optional<User> user = userRepository.getUserById(id);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        User userEntity = user.get();
        userMapper.updateFromDto(userPasswordDTO, userEntity);

        return userRepository.updateUserPassword(userEntity)
                .map(userMapper::toResponse);
    }

    public boolean deleteUser(Long id) {
        if (id == null) {
            return false;
        }
        return userRepository.softDeleteUserById(id);
    }

}
