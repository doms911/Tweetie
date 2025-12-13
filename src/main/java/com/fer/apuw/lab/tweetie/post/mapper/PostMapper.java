package com.fer.apuw.lab.tweetie.post.mapper;

import com.fer.apuw.lab.tweetie.post.dto.PostCreateDTO;
import com.fer.apuw.lab.tweetie.post.dto.PostResponseDTO;
import com.fer.apuw.lab.tweetie.post.dto.PostUpdateDTO;
import com.fer.apuw.lab.tweetie.post.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PostMapper {

    PostResponseDTO toResponse(Post post);

    List<PostResponseDTO> toResponse(List<Post> posts);

    Post toModel(PostCreateDTO postCreateDTO);

    void updateFromDto(PostUpdateDTO postUpdateDTO, @MappingTarget Post post);
}
