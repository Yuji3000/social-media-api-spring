package com.cooksys.socialMediaApi.services.impl;

import com.cooksys.socialMediaApi.entities.Hashtag;
import com.cooksys.socialMediaApi.repositories.HashtagRepository;
import com.cooksys.socialMediaApi.services.HashtagService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;

    @Override
    public Hashtag getTagOrCreateIfNew(String label) {
        Optional<Hashtag> optionalHashtag = hashtagRepository.findByLabel(label);

        if (optionalHashtag.isEmpty()) {
            var newHashTag = new Hashtag();
            newHashTag.setLabel(label);
            return hashtagRepository.saveAndFlush(newHashTag);
        }

        return optionalHashtag.get();
    }
}
