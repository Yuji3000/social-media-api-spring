package com.cooksys.socialMediaApi.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cooksys.socialMediaApi.dtos.TweetResponseDto;
import com.cooksys.socialMediaApi.dtos.UserResponseDto;
import com.cooksys.socialMediaApi.entities.User;
import com.cooksys.socialMediaApi.exceptions.NotFoundException;
import com.cooksys.socialMediaApi.mappers.TweetMapper;
import com.cooksys.socialMediaApi.mappers.UserMapper;
import com.cooksys.socialMediaApi.repositories.UserRepository;
import com.cooksys.socialMediaApi.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;
	private final TweetMapper tweetMapper;
	private final UserRepository userRepository;

	private void validateUserExistsAndActive(String username) {
		if (userRepository.findByCredentialsIgnoreCaseUsernameAndDeletedFalse(username) == null) {
			throw new NotFoundException("User is not found or has been deleted.");
		}
	}

	public List<UserResponseDto> getAllUsers() {
		return userMapper.entitiesToDtos(userRepository.findByDeletedFalse());
	}

	@Override
	public UserResponseDto getUserByUsername(String username) {
		validateUserExistsAndActive(username);
		User userToReturn = userRepository.findByCredentialsIgnoreCaseUsernameAndDeletedFalse(username);
		return userMapper.entityToDto(userToReturn);
	}

	@Override
	public List<TweetResponseDto> getUserMentions(String username) {
		validateUserExistsAndActive(username);
		User userToReturn = userRepository.findByCredentialsIgnoreCaseUsernameAndDeletedFalse(username);
		String mention = "@" + userToReturn.getCredentials().getUsername();
		return tweetMapper.entitiesToDtos(userRepository.findByMentionedUsernameDeletedFalse(mention));
	}
}
