package com.cooksys.socialMediaApi.services;

import com.cooksys.socialMediaApi.entities.Hashtag;
import com.cooksys.socialMediaApi.dtos.HashtagResponseDto;
import java.util.List;

public interface HashtagService {
    List<HashtagResponseDto> getTags();

    Hashtag getTagOrCreateIfNew(String label);
}
