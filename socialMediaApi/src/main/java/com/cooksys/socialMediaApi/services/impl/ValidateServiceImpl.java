package com.cooksys.socialMediaApi.services.impl;

import com.cooksys.socialMediaApi.repositories.HashtagRepository;
import com.cooksys.socialMediaApi.services.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {

    private final HashtagRepository hashtagRepository;

    public boolean validateHashtag(String label) {
        return hashtagRepository.existsByLabelIs("#" + label);
    }

}
