package com.cooksys.socialMediaApi.services;

import com.cooksys.socialMediaApi.entities.Hashtag;

public interface HashtagService {

    Hashtag getTagOrCreateIfNew(String label);
}
