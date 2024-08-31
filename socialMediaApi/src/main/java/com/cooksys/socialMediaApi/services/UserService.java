package com.cooksys.socialMediaApi.services;

import com.cooksys.socialMediaApi.entities.User;
import java.util.List;

import com.cooksys.socialMediaApi.dtos.CredentialsDto;
import com.cooksys.socialMediaApi.dtos.TweetResponseDto;
import com.cooksys.socialMediaApi.dtos.UserRequestDto;
import com.cooksys.socialMediaApi.dtos.UserResponseDto;

public interface UserService {

	User authenticateUser(CredentialsDto credentialsDto);

	List<UserResponseDto> getAllUsers();

	UserResponseDto getUserByUsername(String username);

	User getUserEntityByUsername(String username);

    UserResponseDto createUser(UserRequestDto userRequestDto);

	void saveUser(User user);

	UserResponseDto deleteUser(String username, CredentialsDto credentialsDto);

	List<TweetResponseDto> getTweetsFromUser(String username);

	List<TweetResponseDto> getUserMentions(String username);

	List<UserResponseDto> getFollowingUsers(String username);

	boolean userActive(String username);

	List<UserResponseDto> getFollowers(String username);

	UserResponseDto updateProfile(String username, UserRequestDto userRequestDto);

	void followUser(String user, User follower);

	void unfollowUser(String username, User follower);

	List<TweetResponseDto> getFeed(String username);
}
