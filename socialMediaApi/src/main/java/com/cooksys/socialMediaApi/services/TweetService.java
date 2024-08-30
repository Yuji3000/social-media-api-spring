package com.cooksys.socialMediaApi.services;

import com.cooksys.socialMediaApi.dtos.TweetRequestDto;
import com.cooksys.socialMediaApi.dtos.UserResponseDto;
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

	List<UserResponseDto> getTweetLikes(Long id);

	void likeTweet(Long id, User user);

	TweetResponseDto createTweet(TweetRequestDto tweetRequestDto, User author);
}
