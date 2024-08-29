package com.cooksys.socialMediaApi.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.socialMediaApi.dtos.TweetResponseDto;
import com.cooksys.socialMediaApi.dtos.UserResponseDto;
import com.cooksys.socialMediaApi.services.TweetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {

    private final TweetService tweetService;
    
    @GetMapping
    public List<TweetResponseDto> getAllTweets() {
    	return tweetService.getAllTweets();
    }
    
    @GetMapping("/{id}/mentions")
    public List<UserResponseDto> getTweetMentions(@PathVariable Long id) {
    	return tweetService.getTweetMentions(id);
    }
}
