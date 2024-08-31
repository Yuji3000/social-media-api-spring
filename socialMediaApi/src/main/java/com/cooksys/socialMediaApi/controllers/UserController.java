package com.cooksys.socialMediaApi.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.socialMediaApi.dtos.CredentialsDto;
import com.cooksys.socialMediaApi.dtos.TweetResponseDto;
import com.cooksys.socialMediaApi.dtos.UserRequestDto;
import com.cooksys.socialMediaApi.dtos.UserResponseDto;
import com.cooksys.socialMediaApi.entities.User;
import com.cooksys.socialMediaApi.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	@GetMapping
	public List<UserResponseDto> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/@{username}")
	public UserResponseDto getUserByUsername(@PathVariable String username) {
		return userService.getUserByUsername(username);
	}

	@GetMapping("/@{username}/tweets")
	public List<TweetResponseDto> getTweetsFromUser(@PathVariable String username) {
		return userService.getTweetsFromUser(username);
	}

	@GetMapping("/@{username}/followers")
	public List<UserResponseDto> getFollowers(@PathVariable String username) {
		return userService.getFollowers(username);
	}

	@GetMapping("/@{username}/mentions")
	public List<TweetResponseDto> getUserMentions(@PathVariable String username) {
		return userService.getUserMentions(username);
	}

	@DeleteMapping("/@{username}")
	public UserResponseDto deleteUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
		return userService.deleteUser(username, credentialsDto);
	}

	@PostMapping
	public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
		return userService.createUser(userRequestDto);
	}

    
	@GetMapping("/@{username}/following")
		public List<UserResponseDto> getFollowingUsers(@PathVariable String username) {
		return userService.getFollowingUsers(username);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PostMapping("/@{username}/follow")
	public void followUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
		User follower = userService.authenticateUser(credentialsDto);

		userService.followUser(username, follower);
	}

	@PostMapping("/@{username}/unfollow")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void unfollowUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
		User follower = userService.authenticateUser(credentialsDto);
    
		userService.unfollowUser(username, follower);
	}

	@GetMapping("/@{username}/feed")
	public List<TweetResponseDto> getFeed(@PathVariable String username) {
		return userService.getFeed(username);
	}

	@PatchMapping("/@{username}")
	public UserResponseDto updateProfile(@PathVariable String username, @RequestBody UserRequestDto userRequestDto) {
		return userService.updateProfile(username, userRequestDto);
	}
}
