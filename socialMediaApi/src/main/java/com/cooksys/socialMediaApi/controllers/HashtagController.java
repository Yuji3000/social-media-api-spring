package com.cooksys.socialMediaApi.controllers;

import com.cooksys.socialMediaApi.dtos.TweetResponseDto;
import com.cooksys.socialMediaApi.dtos.HashtagResponseDto;
import com.cooksys.socialMediaApi.services.HashtagService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class HashtagController {

    private final HashtagService hashtagService;

    @GetMapping
    public List<HashtagResponseDto> getTags() {
        return hashtagService.getTags();
    }

    @GetMapping("/{label}")
    public List<TweetResponseDto> tweetsByHashtag(@PathVariable String label) { return hashtagService.tweetsByHashtag(label); }

}
