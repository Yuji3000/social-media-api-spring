package com.cooksys.socialMediaApi.services.impl;

import com.cooksys.socialMediaApi.repositories.HashtagRepository;
import com.cooksys.socialMediaApi.services.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;
}
