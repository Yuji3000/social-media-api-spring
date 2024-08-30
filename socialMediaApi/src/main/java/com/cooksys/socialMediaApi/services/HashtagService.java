package com.cooksys.socialMediaApi.services;

import com.cooksys.socialMediaApi.dtos.TweetResponseDto;
import com.cooksys.socialMediaApi.entities.Hashtag;
import com.cooksys.socialMediaApi.dtos.HashtagResponseDto;
import java.util.List;

public interface HashtagService {

    public List<TweetResponseDto> tweetsByHashtag(String tag);

    List<HashtagResponseDto> getTags();

    Hashtag getTagOrCreateIfNew(String label);
}
