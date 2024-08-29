package com.cooksys.socialMediaApi.services.impl;

import com.cooksys.socialMediaApi.entities.Hashtag;
import com.cooksys.socialMediaApi.repositories.HashtagRepository;
import com.cooksys.socialMediaApi.services.HashtagService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;

    @Override
    public List<Hashtag> getTags() {
        return hashtagRepository.findAll();
    }
}
