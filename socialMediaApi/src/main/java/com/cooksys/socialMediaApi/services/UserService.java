package com.cooksys.socialMediaApi.services;

import com.cooksys.socialMediaApi.dtos.UserResponseDto;

import com.cooksys.socialMediaApi.entities.Credentials;
import java.util.List;

public interface UserService {

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserByUsername(String username);

    UserResponseDto deleteUser(String username, Credentials credentials);

}
