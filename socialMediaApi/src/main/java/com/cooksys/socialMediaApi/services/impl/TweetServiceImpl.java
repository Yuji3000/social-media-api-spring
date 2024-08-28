package com.cooksys.socialMediaApi.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cooksys.socialMediaApi.dtos.TweetResponseDto;
import com.cooksys.socialMediaApi.mappers.TweetMapper;
import com.cooksys.socialMediaApi.repositories.TweetRepository;
import com.cooksys.socialMediaApi.services.TweetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;
    
	@Override
	public List<TweetResponseDto> getAllTweets() {
		return tweetMapper.entitiesToDtos(tweetRepository.findAll());
	}
}
