package com.cooksys.socialMediaApi.controllers;

import com.cooksys.socialMediaApi.dtos.UserRequestDto;
import com.cooksys.socialMediaApi.dtos.CredentialsDto;
import com.cooksys.socialMediaApi.dtos.UserResponseDto;
import com.cooksys.socialMediaApi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @DeleteMapping("/@{username}")
    public UserResponseDto deleteUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
        return userService.deleteUser(username, credentialsDto);
    }
}
