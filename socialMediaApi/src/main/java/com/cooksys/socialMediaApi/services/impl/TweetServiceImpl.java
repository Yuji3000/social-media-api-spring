package com.cooksys.socialMediaApi.services.impl;

import com.cooksys.socialMediaApi.repositories.TweetRepository;
import com.cooksys.socialMediaApi.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
}
