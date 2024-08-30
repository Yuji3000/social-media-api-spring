package com.cooksys.socialMediaApi.services;

import com.cooksys.socialMediaApi.dtos.TweetRequestDto;
import com.cooksys.socialMediaApi.entities.User;
import java.util.List;

import com.cooksys.socialMediaApi.dtos.TweetResponseDto;
import com.cooksys.socialMediaApi.dtos.UserResponseDto;

public interface TweetService {

	TweetResponseDto getTweet(Long id);

	List<TweetResponseDto> getAllTweets();

	List<UserResponseDto> getTweetMentions(Long id);

	List<TweetResponseDto> getAllReposts(Long id);

	TweetResponseDto replyToTweet(Long id, User author, TweetRequestDto tweetRequestDto);

	TweetResponseDto repostTweet(Long id, User author);
}
