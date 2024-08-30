package com.cooksys.socialMediaApi.controllers;


import com.cooksys.socialMediaApi.dtos.TweetRequestDto;
import com.cooksys.socialMediaApi.dtos.TweetResponseDto;
import com.cooksys.socialMediaApi.entities.User;
import com.cooksys.socialMediaApi.services.TweetService;
import com.cooksys.socialMediaApi.services.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {

    private final UserService userService;
    private final TweetService tweetService;
    
    @GetMapping
    public List<TweetResponseDto> getAllTweets() {
    	return tweetService.getAllTweets();
    }
    
    @GetMapping("/{id}/reposts")
    public List<TweetResponseDto> getAllReposts(@PathVariable Long id) {
    	return tweetService.getAllReposts(id);
    }

    @PostMapping("/{id}/reply")
    public TweetResponseDto replyToTweet(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto) {
        User user = userService.authenticateUser(tweetRequestDto.getCredentials());

        return tweetService.replyToTweet(id, user, tweetRequestDto);

    }
}
