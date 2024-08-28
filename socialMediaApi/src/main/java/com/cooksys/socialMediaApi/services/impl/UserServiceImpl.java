package com.cooksys.socialMediaApi.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.socialMediaApi.dtos.UserResponseDto;
import com.cooksys.socialMediaApi.entities.User;
import com.cooksys.socialMediaApi.exceptions.NotFoundException;
import com.cooksys.socialMediaApi.mappers.UserMapper;
import com.cooksys.socialMediaApi.repositories.UserRepository;
import com.cooksys.socialMediaApi.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;
	private final UserRepository userRepository;

	public List<UserResponseDto> getAllUsers() {
		return userMapper.entitiesToDtos(userRepository.findByDeletedFalse());
	}

	@Override
	public UserResponseDto getUserByUsername(String username) {
		if (userRepository.findByCredentialsIgnoreCaseUsernameAndDeletedFalse(username) == null) {
			throw new NotFoundException("User is not found or has been deleted.");
		}
		Optional<User> optionalUser = userRepository.findByCredentialsIgnoreCaseUsernameAndDeletedFalse(username);

		User userToReturn = optionalUser.get();
		return userMapper.entityToDto(userToReturn);
	}
}
