package com.cooksys.socialMediaApi.services;

import com.cooksys.socialMediaApi.dtos.UserResponseDto;

import java.util.List;

public interface UserService {

    public List<UserResponseDto> getAllUsers();

}
