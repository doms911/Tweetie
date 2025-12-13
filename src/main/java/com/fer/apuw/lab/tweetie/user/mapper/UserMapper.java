package com.fer.apuw.lab.tweetie.user.mapper;

import com.fer.apuw.lab.tweetie.user.dto.UserCreateDTO;
import com.fer.apuw.lab.tweetie.user.dto.UserPasswordDTO;
import com.fer.apuw.lab.tweetie.user.dto.UserRequestDTO;
import com.fer.apuw.lab.tweetie.user.dto.UserResponseDTO;
import com.fer.apuw.lab.tweetie.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserResponseDTO toResponse(User user);

    List<UserResponseDTO> toResponse(List<User> users);

    @Mapping(target = "username", expression = "java(user.getUsername().toLowerCase())")
    @Mapping(target = "email", expression = "java(user.getEmail().toLowerCase())")
    @Mapping(target = "roleType", expression = "java(user.getRoleType().name().toUpperCase())")
    User toModel(UserCreateDTO userCreateDTO);

    @Mapping(target = "username", expression = "java(user.getUsername().toLowerCase())")
    @Mapping(target = "email", expression = "java(user.getEmail().toLowerCase())")
    @Mapping(target = "roleType", expression = "java(user.getRoleType().name().toUpperCase())")
    void updateFromDto(UserRequestDTO userCreateDTO, @MappingTarget User user);

    void updateFromDto(UserPasswordDTO userPasswordDTO, @MappingTarget User user);
}
