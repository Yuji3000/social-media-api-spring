package com.cooksys.socialMediaApi.services;

import java.util.List;

import com.cooksys.socialMediaApi.dtos.TweetResponseDto;
import com.cooksys.socialMediaApi.dtos.UserResponseDto;

public interface TweetService {

	List<TweetResponseDto> getAllTweets();

	List<UserResponseDto> getTweetMentions(Long id);
}
