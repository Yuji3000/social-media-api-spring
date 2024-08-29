package com.cooksys.socialMediaApi.services;

import java.util.List;

import com.cooksys.socialMediaApi.dtos.CredentialsDto;
import com.cooksys.socialMediaApi.dtos.TweetResponseDto;
import com.cooksys.socialMediaApi.dtos.UserRequestDto;
import com.cooksys.socialMediaApi.dtos.UserResponseDto;

public interface UserService {

	List<UserResponseDto> getAllUsers();

	UserResponseDto getUserByUsername(String username);

	UserResponseDto deleteUser(String username, CredentialsDto credentialsDto);

	public List<TweetResponseDto> getUserMentions(String username);

    UserResponseDto createUser(UserRequestDto userRequestDto);
}
