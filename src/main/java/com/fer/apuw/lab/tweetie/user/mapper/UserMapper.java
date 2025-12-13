package com.fer.apuw.lab.tweetie.user.mapper;

import com.fer.apuw.lab.tweetie.user.dto.UserCreateDTO;
import com.fer.apuw.lab.tweetie.user.dto.UserPasswordDTO;
import com.fer.apuw.lab.tweetie.user.dto.UserRequestDTO;
import com.fer.apuw.lab.tweetie.user.dto.UserResponseDTO;
import com.fer.apuw.lab.tweetie.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserResponseDTO toResponse(User user);

    List<UserResponseDTO> toResponse(List<User> users);

    User toModel(UserCreateDTO userCreateDTO);

    void updateFromDto(UserRequestDTO userCreateDTO, @MappingTarget User user);

    void updateFromDto(UserPasswordDTO userPasswordDTO, @MappingTarget User user);
}
