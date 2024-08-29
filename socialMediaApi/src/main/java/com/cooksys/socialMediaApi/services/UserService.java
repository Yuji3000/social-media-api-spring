package com.cooksys.socialMediaApi.services;

import com.cooksys.socialMediaApi.dtos.CredentialsDto;
import com.cooksys.socialMediaApi.dtos.UserResponseDto;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserByUsername(String username);

    UserResponseDto deleteUser(String username, CredentialsDto credentialsDto);

}
