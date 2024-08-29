package com.cooksys.socialMediaApi.services.impl;

import com.cooksys.socialMediaApi.dtos.UserResponseDto;
import com.cooksys.socialMediaApi.entities.Credentials;
import com.cooksys.socialMediaApi.entities.User;
import com.cooksys.socialMediaApi.exceptions.BadRequestException;
import com.cooksys.socialMediaApi.exceptions.NotAuthorizedException;
import com.cooksys.socialMediaApi.exceptions.NotFoundException;
import com.cooksys.socialMediaApi.mappers.UserMapper;
import com.cooksys.socialMediaApi.repositories.UserRepository;
import com.cooksys.socialMediaApi.services.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    private void validateCredentials(Credentials credentials) {
        if (credentials == null || credentials.getUsername() == null || credentials.getPassword() == null
            || credentials.getUsername().isBlank() || credentials.getPassword().isBlank()) {
            throw new BadRequestException("Full credentials required");
        }
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userMapper.entitiesToDtos(userRepository.findByDeletedFalse());
    }

    @Override
    public UserResponseDto deleteUser(String username, Credentials credentials) {
        validateCredentials(credentials);

        if (!credentials.getUsername().equals(username)) {
            throw new NotAuthorizedException("User to delete does not match user in given credentials");
        }

        Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);

        if (optionalUser.isEmpty() || optionalUser.get().isDeleted()) {
            throw new NotFoundException("User not found");
        }

        User user = optionalUser.get();

        if (!credentials.getPassword().equals(user.getCredentials().getPassword())) {
            throw new NotAuthorizedException("Invalid credentials");
        }

        user.setDeleted(true);
        userRepository.saveAndFlush(user);

        return userMapper.entityToDto(user);
    }
}
