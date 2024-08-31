package com.cooksys.socialMediaApi.services.impl;

import com.cooksys.socialMediaApi.dtos.TweetResponseDto;
import com.cooksys.socialMediaApi.exceptions.NotFoundException;
import com.cooksys.socialMediaApi.mappers.TweetMapper;
import com.cooksys.socialMediaApi.entities.Hashtag;
import com.cooksys.socialMediaApi.dtos.HashtagResponseDto;
import com.cooksys.socialMediaApi.mappers.HashtagMapper;
import com.cooksys.socialMediaApi.repositories.HashtagRepository;
import com.cooksys.socialMediaApi.repositories.TweetRepository;
import com.cooksys.socialMediaApi.services.HashtagService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagMapper hashtagMapper;
    private final HashtagRepository hashtagRepository;
    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;

    @Override
    public List<HashtagResponseDto> getTags() {
        return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
    }

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

    public List<TweetResponseDto> tweetsByHashtag(String label) throws NotFoundException {
        if (!hashtagRepository.existsByLabel(label)) { throw new NotFoundException("Hashtag not found :#" + label); }

        return tweetMapper.entitiesToDtos(tweetRepository.findByDeletedFalseAndHashtagsLabelOrderByPostedDesc(label));
    }

}
