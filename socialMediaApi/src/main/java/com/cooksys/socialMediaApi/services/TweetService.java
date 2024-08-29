package com.cooksys.socialMediaApi.services;

import java.util.List;

import com.cooksys.socialMediaApi.dtos.TweetRepostResponseDto;
import com.cooksys.socialMediaApi.dtos.TweetResponseDto;

public interface TweetService {

	List<TweetResponseDto> getAllTweets();

	List<TweetResponseDto> getAllReposts(Long id);
}
