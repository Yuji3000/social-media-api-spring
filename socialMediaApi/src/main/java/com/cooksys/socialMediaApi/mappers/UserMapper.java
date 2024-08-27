package com.cooksys.socialMediaApi.mappers;

import com.cooksys.socialMediaApi.dtos.UserRequestDto;
import com.cooksys.socialMediaApi.dtos.UserResponseDto;
import com.cooksys.socialMediaApi.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto entityToDto(User entity);

    User requestDtoToEntity(UserRequestDto userRequestDto);

    List<UserResponseDto> entitiesToDtos(List<User> entities);
}
