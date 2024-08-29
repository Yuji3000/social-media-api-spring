package com.cooksys.socialMediaApi.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cooksys.socialMediaApi.dtos.CredentialsDto;
import com.cooksys.socialMediaApi.dtos.UserRequestDto;
import com.cooksys.socialMediaApi.dtos.UserResponseDto;
import com.cooksys.socialMediaApi.entities.User;
import com.cooksys.socialMediaApi.exceptions.BadRequestException;
import com.cooksys.socialMediaApi.exceptions.ConflictException;
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

    private void validateCreateUserRequest(UserRequestDto userRequestDto) {
        var credentials = userRequestDto.getCredentials();
        var profile = userRequestDto.getProfile();

        if (credentials == null || credentials.getUsername() == null || credentials.getPassword() == null
            || profile == null || profile.getEmail() == null) {
            throw new BadRequestException("Missing required fields");
        }

        if (credentials.getUsername().isBlank() || credentials.getPassword().isBlank()
            || profile.getEmail().isBlank()) {
            throw new BadRequestException("Required fields cannot be blank");
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
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        validateCreateUserRequest(userRequestDto);

        String username = userRequestDto.getCredentials().getUsername();
        Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            if (existingUser.isDeleted()) {
                existingUser.setDeleted(false);
                return userMapper.entityToDto(userRepository.saveAndFlush(existingUser));
            } else {
                throw new ConflictException("User with the username '" + username + "' already exists");
            }
        }

        User newUser = userMapper.requestDtoToEntity(userRequestDto);
        return userMapper.entityToDto(userRepository.saveAndFlush(newUser));
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
		
		 List<User> followers = user.getFollowers()
		            .stream()
		            .filter(follower -> !follower.isDeleted())
		            .collect(Collectors.toList());
		
		return userMapper.entitiesToDtos(followers);
	}
}
