package com.cooksys.socialMediaApi.services.impl;

import com.cooksys.socialMediaApi.entities.Credentials;
import com.cooksys.socialMediaApi.entities.Profile;
import com.cooksys.socialMediaApi.entities.Tweet;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cooksys.socialMediaApi.repositories.TweetRepository;
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
import com.cooksys.socialMediaApi.mappers.CredentialsMapper;
import com.cooksys.socialMediaApi.repositories.UserRepository;
import com.cooksys.socialMediaApi.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;
	private final TweetMapper tweetMapper;
    private final CredentialsMapper credentialsMapper;
	private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    /**
     * Gets all tweets in a user's feed (their tweets + tweets of followed users)
     * sorted by date. Dates use descending order.
     *
     * @param username The user to get the feed for.
     * @return All tweets in the feed.
     */
    @Override
    public List<TweetResponseDto> getFeed(String username) {
        User user = getUserEntityByUsername(username);

        List<Tweet> feed = tweetRepository.findByDeletedFalseAndAuthorOrAuthorInOrderByPostedDesc(user, user.getFollowing());

        return tweetMapper.entitiesToDtos(feed);
    }

    /**
     * Adds a following relationship between the follower and a user. An
     * exception is thrown if the relationship already exists.
     *
     * @param username The user to be followed.
     * @param follower The follower.
     */
    @Override
    public void followUser(String username, User follower) {
        User user = getUserEntityByUsername(username);

        List<User> following = follower.getFollowing();
        if (!following.contains(user)) {
            following.add(user);
        } else {
            throw new BadRequestException("This user is already followed");
        }

        userRepository.saveAndFlush(follower);
    }

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

    /**
     * Updates a user's profile. An error is thrown if the credentials do not
     * match the username.
     *
     * @param username The user to be updated
     * @param userRequestDto The credentials and updated profile
     * @return Updated user profile
     */
    @Override
    public UserResponseDto updateProfile(String username, UserRequestDto userRequestDto) {
        User userRequest =  userMapper.requestDtoToEntity(userRequestDto);

        Credentials credentials = userRequest.getCredentials();

        User authenticated = authenticateUser(credentialsMapper.entityToDto(credentials));

        if (!Objects.equals(username, authenticated.getCredentials().getUsername())) {
            throw new BadRequestException("The credentials must match the user being updated.");
        }

        Profile providedProfile = userRequest.getProfile();

        if (providedProfile == null) {
            throw new BadRequestException("A profile must be provided");
        }

        Profile existingProfile = authenticated.getProfile();

        // Only set provided values if not null
        String firstName = providedProfile.getFirstName();
        String lastName = providedProfile.getLastName();
        String email = providedProfile.getEmail();
        String phone = providedProfile.getPhone();
        if (firstName != null) {
            existingProfile.setFirstName(firstName);
        }
        if (lastName != null) {
            existingProfile.setLastName(lastName);
        }
        if (email != null) {
            existingProfile.setEmail(email);
        }
        if (phone != null) {
            existingProfile.setPhone(phone);
        }

        return userMapper.entityToDto(userRepository.saveAndFlush(authenticated));
    }

    @Override
    public User authenticateUser(CredentialsDto credentialsDto) {
        if (credentialsDto == null) {
            throw new BadRequestException("Credentials must be provided");
        }

        Optional<User> optionalUser = userRepository
            .findByCredentialsIgnoreCaseUsernameAndDeletedFalse(credentialsDto.getUsername());

        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        User user = optionalUser.get();

        if (credentialsDto.getPassword() == null || credentialsDto.getUsername() == null) {
            throw new BadRequestException("Username or password should not be null");
        }
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
    public void saveUser(User user) {
        userRepository.saveAndFlush(user);
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

		 List<User> followers = user.getFollowing()
		            .stream()
		            .filter(userFollowing -> !userFollowing.isDeleted())
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
