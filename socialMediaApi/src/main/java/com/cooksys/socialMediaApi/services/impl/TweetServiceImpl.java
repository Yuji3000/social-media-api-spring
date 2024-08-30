package com.cooksys.socialMediaApi.services.impl;

import com.cooksys.socialMediaApi.entities.Hashtag;
import com.cooksys.socialMediaApi.entities.User;
import com.cooksys.socialMediaApi.services.HashtagService;
import com.cooksys.socialMediaApi.services.UserService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import com.cooksys.socialMediaApi.dtos.TweetRequestDto;
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
	private final UserService userService;
	private final HashtagService hashtagService;
  
	private Tweet getTweet(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);
		if (optionalTweet.isEmpty()) {
			throw new NotFoundException("No Tweet with id: " + id);
		}
		return optionalTweet.get();
	}

	/**
	 * Finds hashtags within a given string. The following rules decide which hashtags are valid:
	 * 1. The word must start with a '#'
	 * 2. The hashtag ends once a non-word character is encountered (e.g. #Hi!Friend -> #Hi, ##Goals -> #)
	 * 3. After sanitizing, the hashtag after the '#' prefix must not be empty, otherwise it will be dropped
	 *
	 * @param content the raw string contents of the tweet
	 * @return a list of hashtag entities
	 */
	private List<Hashtag> getHashtags(String content) {
		String sanitizedContent = content.replaceAll("[^#\\w]", " ");

		return Arrays.stream(sanitizedContent.split("\\s+"))
			.filter(word -> word.startsWith("#") && word.length() > 1)
			.map(hashtag -> hashtag.substring(1))
			.map(hashtagService::getTagOrCreateIfNew)
			.collect(Collectors.toList());
	}


	/**
	 * Finds mentioned users within a given string. The following rules decide which mentions are valid:
	 * 1. The word must start with a '@'
	 * 2. The mention ends once a non-word character is encountered (e.g. @John$Doe -> @John, @@John -> @)
	 * 3. After sanitizing, the mention after the '@' prefix must not be empty, otherwise it will be dropped
	 * 4. Users who do not exist or have been deleted are not included in this list
	 *
	 * @param content the raw string contents of the tweet
	 * @return a list of User entities identified without their '#' prefix
	 */
	private List<User> getMentionedUsers(String content) {
		String sanitizedContent = content.replaceAll("[^#\\w]", " ");

		return Arrays.stream(sanitizedContent.split("\\s"))
			.filter(word -> word.startsWith("@") && word.length() > 1)
			.map(mention -> mention.substring(1))
			.filter(userService::userActive)
			.map(userService::getUserEntityByUsername)
			.collect(Collectors.toList());
	}

	@Override
	public List<TweetResponseDto> getAllTweets() {
		return tweetMapper.entitiesToDtos(tweetRepository.findByDeletedFalseOrderByPostedDesc());
	}

	@Override
	public TweetResponseDto repostTweet(Long id, User author) {
		Optional<Tweet> optionalTweetToRepost = tweetRepository.findByIdAndDeletedFalse(id);

		if (optionalTweetToRepost.isEmpty()) {
			throw new NotFoundException("Tweet to repost not found");
		}

		Tweet repost = new Tweet();
		repost.setAuthor(author);
		repost.setRepostOf(optionalTweetToRepost.get());

		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(repost));
	}

	@Override
	public List<TweetResponseDto> getDirectRepliesForTweet(Long id) {
		Tweet tweet = getTweet(id);

		List<TweetResponseDto> replies = tweet.getReplies().stream()
			.filter(reply -> !reply.isDeleted())
			.map(tweetMapper::entityToDto)
			.toList();

		replies.forEach(reply -> reply.getInReplyTo().setInReplyTo(null));

		return replies;
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

	@Override
	public TweetResponseDto replyToTweet(Long id, User author, TweetRequestDto tweetRequestDto) {
		Optional<Tweet> optionalTweetToReplyTo = tweetRepository.findByIdAndDeletedFalse(id);

		if (optionalTweetToReplyTo.isEmpty()) {
			throw new NotFoundException("Tweet to reply to not found");
		}

		Tweet reply = tweetMapper.requestDtoToEntity(tweetRequestDto);
		reply.setAuthor(author);
		reply.setInReplyTo(optionalTweetToReplyTo.get());
		reply.setHashtags(getHashtags(reply.getContent()));
		reply.setMentionedUsers(getMentionedUsers(reply.getContent()));

		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(reply));
	}
}
