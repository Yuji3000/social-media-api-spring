package com.cooksys.socialMediaApi.services;

import com.cooksys.socialMediaApi.dtos.TweetRequestDto;
import com.cooksys.socialMediaApi.entities.User;
import java.util.List;

import com.cooksys.socialMediaApi.dtos.TweetResponseDto;
import com.cooksys.socialMediaApi.dtos.UserResponseDto;

public interface TweetService {

	List<TweetResponseDto> getAllTweets();

	List<UserResponseDto> getTweetMentions(Long id);

	List<TweetResponseDto> getAllReposts(Long id);

	TweetResponseDto replyToTweet(Long id, User author, TweetRequestDto tweetRequestDto);

	TweetResponseDto repostTweet(Long id, User author);

	TweetResponseDto deleteTweet(Long id, User author);

	TweetResponseDto createTweet(TweetRequestDto tweetRequestDto, User author);

}
