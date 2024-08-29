package com.cooksys.socialMediaApi.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.socialMediaApi.dtos.UserRequestDto;
import com.cooksys.socialMediaApi.dtos.UserResponseDto;
import com.cooksys.socialMediaApi.entities.User;
import com.cooksys.socialMediaApi.exceptions.BadRequestException;
import com.cooksys.socialMediaApi.exceptions.ConflictException;
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
		if (userRepository.findByCredentialsIgnoreCaseUsernameAndDeletedFalse(username) == null) {
			throw new NotFoundException("User is not found or has been deleted.");
		}
		Optional<User> optionalUser = userRepository.findByCredentialsIgnoreCaseUsernameAndDeletedFalse(username);

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
}
