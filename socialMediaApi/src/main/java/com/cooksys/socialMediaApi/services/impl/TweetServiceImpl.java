package com.cooksys.socialMediaApi.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cooksys.socialMediaApi.dtos.TweetRepostResponseDto;
import com.cooksys.socialMediaApi.dtos.TweetResponseDto;
import com.cooksys.socialMediaApi.entities.Tweet;
import com.cooksys.socialMediaApi.exceptions.NotFoundException;
import com.cooksys.socialMediaApi.mappers.TweetMapper;
import com.cooksys.socialMediaApi.repositories.TweetRepository;
import com.cooksys.socialMediaApi.services.TweetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;

	private Tweet getTweet(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);
		if (optionalTweet.isEmpty()) {
			throw new NotFoundException("No Tweet with id: " + id);
		}
		return optionalTweet.get();
	}

	@Override
	public List<TweetResponseDto> getAllTweets() {
		return tweetMapper.entitiesToDtos(tweetRepository.findByDeletedFalseOrderByPostedDesc());
	}

	@Override
	public List<TweetResponseDto> getAllReposts(Long id) {
		Tweet originalTweet = getTweet(id);

		List<Tweet> filteredTweets = originalTweet.getReposts()
				.stream()
				.filter(repost -> !repost.isDeleted())
				.collect(Collectors.toList());
		
		List<TweetResponseDto> tweetResponse = tweetMapper.entitiesToDtos(filteredTweets);
		
		for (TweetResponseDto dto : tweetResponse) {
			dto.setInReplyTo(null);
			dto.setRepostOf(null);
		}
		
		return tweetResponse;
	}
}
