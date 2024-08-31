package com.cooksys.socialMediaApi.services.impl;

import com.cooksys.socialMediaApi.dtos.ContextDto;
import com.cooksys.socialMediaApi.entities.Hashtag;
import com.cooksys.socialMediaApi.entities.User;
import com.cooksys.socialMediaApi.services.HashtagService;
import com.cooksys.socialMediaApi.services.UserService;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cooksys.socialMediaApi.dtos.HashtagResponseDto;
import com.cooksys.socialMediaApi.dtos.TweetRequestDto;
import com.cooksys.socialMediaApi.dtos.TweetResponseDto;
import com.cooksys.socialMediaApi.dtos.UserResponseDto;
import com.cooksys.socialMediaApi.entities.Tweet;
import com.cooksys.socialMediaApi.exceptions.NotAuthorizedException;
import com.cooksys.socialMediaApi.exceptions.BadRequestException;
import com.cooksys.socialMediaApi.exceptions.NotFoundException;
import com.cooksys.socialMediaApi.mappers.HashtagMapper;
import com.cooksys.socialMediaApi.mappers.TweetMapper;
import com.cooksys.socialMediaApi.mappers.UserMapper;
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
	private final HashtagMapper hashtagMapper;
	private final UserMapper userMapper;

	private Tweet getTweetEntity(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);
		if (optionalTweet.isEmpty()) {
			throw new NotFoundException("No Tweet with id: " + id);
		}
		return optionalTweet.get();
	}

	/**
	 * Gets all active users who liked the given tweet.
	 *
	 * @param id The ID of the tweet.
	 * @return A list of active users the tweet was liked by.
	 */
	@Override
	public List<UserResponseDto> getTweetLikes(Long id) {
		Tweet tweet = getTweetEntity(id);

		List<User> activeUsers = new ArrayList<>();

		for (User user: tweet.getLikedByUsers()) {
			if (userService.userActive(user.getCredentials().getUsername())) {
				activeUsers.add(user);
			}
		}

		return userMapper.entitiesToDtos(activeUsers);
	}

	/**
	 * Gets all hashtags found in a tweet.
	 *
	 * @param id The ID of the tweet.
	 * @return A list of the tweet's tags.
	 */
	@Override
	public List<HashtagResponseDto> getTweetTags(Long id) {
		Tweet tweet = getTweetEntity(id);

		return hashtagMapper.entitiesToDtos(tweet.getHashtags());
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
		String sanitizedContent = content.replaceAll("[^@\\w]", " ");

		return Arrays.stream(sanitizedContent.split("\\s"))
			.filter(word -> word.startsWith("@") && word.length() > 1)
			.map(mention -> mention.substring(1))
			.filter(userService::userActive)
			.map(userService::getUserEntityByUsername)
			.collect(Collectors.toList());
	}

	@Override
	public TweetResponseDto getTweet(Long id) {
		return tweetMapper.entityToDto(getTweetEntity(id));
	}

	@Override
	public List<TweetResponseDto> getAllTweets() {
		return tweetMapper.entitiesToDtos(tweetRepository.findByDeletedFalseOrderByPostedDesc());
	}

	@Override
	public List<UserResponseDto> getTweetMentions(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);

		if (optionalTweet.isEmpty()) {
			throw new NotFoundException("Tweet not found with ID:" + id);
		}

		Tweet tweet = optionalTweet.get();
		List<User> mentionedUsers = tweet.getMentionedUsers().stream().filter(user -> !user.isDeleted())
				.collect(Collectors.toList());

		return userMapper.entitiesToDtos(mentionedUsers);
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
		Tweet tweet = getTweetEntity(id);

		List<TweetResponseDto> replies = tweet.getReplies().stream()
			.filter(reply -> !reply.isDeleted())
			.map(tweetMapper::entityToDto)
			.toList();

		replies.forEach(reply -> {
			reply.setInReplyTo(null);
			reply.setRepostOf(null);
		});

		return replies;
	}

	/**
	 * Adds a tweet to the user's list of liked tweets. Nothing happens if the
	 * tweet has already been liked. An exception is thrown if the tweet does
	 * not exist.
	 *
	 * @param id the id of the tweet to like
	 * @param user the user liking the tweet
	 */
	@Override
	public void likeTweet(Long id, User user) {
		Optional<Tweet> optionalTweetToLike = tweetRepository.findByIdAndDeletedFalse(id);

		if (optionalTweetToLike.isEmpty()) {
			throw new NotFoundException("Tweet to like not found");
		}

		Tweet tweetToLike = optionalTweetToLike.get();
		List<Tweet> likedTweets = user.getLikedTweets();
		if (!likedTweets.contains(tweetToLike)) {
			likedTweets.add(tweetToLike);
		}

		userService.saveUser(user);
	}

	@Override
	public ContextDto getTweetContext(Long id) {
		Tweet tweet = getTweetEntity(id);

		List<Tweet> flattenedReplies = new ArrayList<>();
		collectFlattenedReplies(tweet, flattenedReplies);
		flattenedReplies.sort(Comparator.comparing(Tweet::getPosted));

		List<Tweet> tweetsRepliedToChain = new ArrayList<>();
		collectTweetsRepliedToChain(tweet, tweetsRepliedToChain);

		ContextDto contextDto = new ContextDto();
		contextDto.setTarget(tweetMapper.entityToDto(tweet));
		contextDto.setBefore(tweetMapper.entitiesToDtos(tweetsRepliedToChain));
		contextDto.setAfter(tweetMapper.entitiesToDtos(flattenedReplies));

		return contextDto;
	}

	private void collectTweetsRepliedToChain(Tweet tweet, List<Tweet> tweetsRepliedToChain) {
		for (Tweet parentTweet = tweet.getInReplyTo(); parentTweet != null; parentTweet = parentTweet.getInReplyTo()) {
			if (!parentTweet.isDeleted()) {
				tweetsRepliedToChain.add(parentTweet);
			}
		}
	}

	private void collectFlattenedReplies(Tweet tweet, List<Tweet> flattenedReplies) {
		for (Tweet reply : tweet.getReplies()) {
			if (!reply.isDeleted()) {
				flattenedReplies.add(reply);
			}

			collectFlattenedReplies(reply, flattenedReplies);
		}
	}

	@Override
	public List<TweetResponseDto> getAllReposts(Long id) {
		Tweet originalTweet = getTweetEntity(id);

		List<Tweet> filteredTweets = originalTweet.getReposts()
				.stream()
				.filter(repost -> !repost.isDeleted())
				.collect(Collectors.toList());

		List<TweetResponseDto> tweetResponse = tweetMapper.entitiesToDtos(filteredTweets);

		for (TweetResponseDto dto : tweetResponse) {
			dto.setInReplyTo(null);
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

	@Override
	public TweetResponseDto deleteTweet(Long id, User author) {
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);

		if (optionalTweet.isEmpty()) {
			throw new NotFoundException("Tweet not found with ID:" + id);
		}

		Tweet tweetToDelete = optionalTweet.get();
		if (!tweetToDelete.getAuthor().equals(author)) {
			throw new NotAuthorizedException("User is not authorized to delete this tweet");
		}

		tweetToDelete.setDeleted(true);
		tweetRepository.save(tweetToDelete);

		return tweetMapper.entityToDto(tweetToDelete);
	 }

	@Override
	public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto, User author) {
		if (tweetRequestDto == null) {
			throw new BadRequestException("Tweet request must be provided");
		}
		if (tweetRequestDto.getContent() == null) {
			throw new BadRequestException("Tweet content cannot be null");
		}
		if (tweetRequestDto.getContent().isEmpty()) {
			throw new BadRequestException("Tweet content cannot be empty");
		}

		Tweet tweet = tweetMapper.requestDtoToEntity(tweetRequestDto);

		tweet.setAuthor(author);
		tweet.setInReplyTo(null);
		tweet.setRepostOf(null);
		tweet.setHashtags(getHashtags(tweet.getContent()));
		tweet.setMentionedUsers(getMentionedUsers(tweet.getContent()));


		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweet));
	 }
}
