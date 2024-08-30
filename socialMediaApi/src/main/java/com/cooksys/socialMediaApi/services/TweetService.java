package com.cooksys.socialMediaApi.services;

import com.cooksys.socialMediaApi.dtos.TweetRequestDto;
import com.cooksys.socialMediaApi.entities.User;
import java.util.List;

import com.cooksys.socialMediaApi.dtos.TweetRepostResponseDto;
import com.cooksys.socialMediaApi.dtos.TweetResponseDto;

public interface TweetService {

	List<TweetResponseDto> getAllTweets();

	List<TweetRepostResponseDto> getAllReposts(Long id);

	TweetResponseDto replyToTweet(Long id, User author, TweetRequestDto tweetRequestDto);

}
