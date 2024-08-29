package com.cooksys.socialMediaApi.controllers;

import com.cooksys.socialMediaApi.dtos.TweetResponseDto;
import com.cooksys.socialMediaApi.dtos.UserResponseDto;
import com.cooksys.socialMediaApi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponseDto> getAllUsers() { return userService.getAllUsers(); }
    
    @GetMapping("/@{username}")
    public UserResponseDto getUserByUsername(@PathVariable String username) {
    	return userService.getUserByUsername(username);
    }
    
    @GetMapping("/@{username}/mentions")
    public List<TweetResponseDto> getUserMentions(@PathVariable String username ) {
    	return (List<TweetResponseDto>) userService.getUserMentions(username);
    }
    
}
