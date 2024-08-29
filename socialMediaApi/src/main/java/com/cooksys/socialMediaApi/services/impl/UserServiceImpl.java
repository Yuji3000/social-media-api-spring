package com.cooksys.socialMediaApi.services.impl;

import com.cooksys.socialMediaApi.dtos.CredentialsDto;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.socialMediaApi.dtos.UserResponseDto;
import com.cooksys.socialMediaApi.entities.User;
import com.cooksys.socialMediaApi.exceptions.BadRequestException;
import com.cooksys.socialMediaApi.exceptions.NotAuthorizedException;
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

    private void validateCredentials(CredentialsDto credentialsDto) {
        if (credentialsDto == null || credentialsDto.getUsername() == null || credentialsDto.getPassword() == null
            || credentialsDto.getUsername().isBlank() || credentialsDto.getPassword().isBlank()) {
            throw new BadRequestException("Full credentials required");
        }
    }

    @Override
	public List<UserResponseDto> getAllUsers() {
		return userMapper.entitiesToDtos(userRepository.findByDeletedFalse());
	}

	@Override
	public UserResponseDto getUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByCredentialsIgnoreCaseUsernameAndDeletedFalse(username);

		if (optionalUser.isEmpty()) {
			throw new NotFoundException("User is not found or has been deleted.");
		}

		User userToReturn = optionalUser.get();
		return userMapper.entityToDto(userToReturn);
	}

    @Override
    public UserResponseDto deleteUser(String username, CredentialsDto credentialsDto) {
        validateCredentials(credentialsDto);

        if (!credentialsDto.getUsername().equalsIgnoreCase(username)) {
            throw new NotAuthorizedException("User to delete does not match user in given credentials");
        }

        Optional<User> optionalUser = userRepository.findByCredentialsIgnoreCaseUsernameAndDeletedFalse(username);

        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        User user = optionalUser.get();

        if (!credentialsDto.getPassword().equals(user.getCredentials().getPassword())) {
            throw new NotAuthorizedException("Invalid credentials");
        }

        user.setDeleted(true);
        userRepository.saveAndFlush(user);

        return userMapper.entityToDto(user);
    }

	@Override
	public List<UserResponseDto> getFollowingUsers(String username) {
		Optional<User> optionalUser = userRepository.findByCredentialsIgnoreCaseUsernameAndDeletedFalse(username);

		if (optionalUser.isEmpty()) {
			throw new NotFoundException("User is not found or has been deleted.");
		}
		
		User user = optionalUser.get();
		
		return userMapper.entitiesToDtos(user.getFollowers());
	}
}
