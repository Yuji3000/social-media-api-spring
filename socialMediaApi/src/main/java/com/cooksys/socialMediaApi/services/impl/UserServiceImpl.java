package com.cooksys.socialMediaApi.services.impl;

import com.cooksys.socialMediaApi.entities.Tweet;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cooksys.socialMediaApi.dtos.CredentialsDto;
import com.cooksys.socialMediaApi.dtos.TweetResponseDto;
import com.cooksys.socialMediaApi.dtos.UserRequestDto;
import com.cooksys.socialMediaApi.dtos.UserResponseDto;
import com.cooksys.socialMediaApi.entities.User;
import com.cooksys.socialMediaApi.exceptions.BadRequestException;
import com.cooksys.socialMediaApi.exceptions.ConflictException;
import com.cooksys.socialMediaApi.exceptions.NotAuthorizedException;
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
		if (userRepository.findByCredentialsIgnoreCaseUsernameAndDeletedFalse(username).isEmpty()) {
			throw new NotFoundException("User is not found or has been deleted.");
		}
	}


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
    public User authenticateUser(CredentialsDto credentialsDto) {
        Optional<User> optionalUser = userRepository
            .findByCredentialsIgnoreCaseUsernameAndDeletedFalse(credentialsDto.getUsername());

        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        User user = optionalUser.get();

        if (!credentialsDto.getPassword().equals(user.getCredentials().getPassword())) {
            throw new NotAuthorizedException("Invalid credentials");
        }

        return user;
    }

    @Override
	public List<UserResponseDto> getAllUsers() {
		return userMapper.entitiesToDtos(userRepository.findByDeletedFalse());
	}

    @Override
	public UserResponseDto getUserByUsername(String username) {
		return userMapper.entityToDto(getUserEntityByUsername(username));
	}

    @Override
    public User getUserEntityByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByCredentialsIgnoreCaseUsernameAndDeletedFalse(username);

        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User is not found or has been deleted.");
        }

        return optionalUser.get();
    }

	@Override
	public List<TweetResponseDto> getUserMentions(String username) {
		validateUserExistsAndActive(username);
		Optional<User> optionalUser = userRepository.findByCredentialsIgnoreCaseUsernameAndDeletedFalse(username);
		
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("User is not found or has been deleted.");
		}
		
		String userName = optionalUser.get().getCredentials().getUsername();

		return tweetMapper.entitiesToDtos(userRepository.findByMentionedUsernameDeletedFalse(userName));

	}

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        validateCreateUserRequest(userRequestDto);

        String username = userRequestDto.getCredentials().getUsername();
        Optional<User> optionalUser = userRepository.findByCredentialsIgnoreCaseUsername(username);

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

        User user = authenticateUser(credentialsDto);

        user.setDeleted(true);
        userRepository.saveAndFlush(user);

        return userMapper.entityToDto(user);
    }

    @Override
    public List<TweetResponseDto> getTweetsFromUser(String username) {
        if (!userActive(username)) {
            throw new NotFoundException("User not found");
        }

        User user = getUserEntityByUsername(username);

        return user.getTweets().stream()
            .filter(tweet -> !tweet.isDeleted())
            .sorted(Comparator.comparing(Tweet::getPosted).reversed())
            .map(tweetMapper::entityToDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getFollowers(String username) {
        if (!userActive(username)) {
            throw new NotFoundException("User not found");
        }

        User user = getUserEntityByUsername(username);

        return user.getFollowers().stream()
            .filter(follower -> !follower.isDeleted())
            .map(userMapper::entityToDto)
            .collect(Collectors.toList());
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

    @Override
    public boolean userActive(String username) {
        return userRepository.existsByCredentialsIgnoreCaseUsernameAndDeletedIsFalse(username);
    }


	@Override
	public void unfollowUser(String username, User follower) {
		User user = getUserEntityByUsername(username);

        List<User> following = follower.getFollowing();
        if (following.contains(user)) {
            following.remove(user);
        } else {
            throw new BadRequestException("This user is already unfollowed");
        }

        userRepository.saveAndFlush(follower);
	}
}
