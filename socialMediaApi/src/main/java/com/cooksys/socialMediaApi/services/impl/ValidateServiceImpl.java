package com.cooksys.socialMediaApi.services.impl;

import com.cooksys.socialMediaApi.repositories.HashtagRepository;
import com.cooksys.socialMediaApi.repositories.UserRepository;
import com.cooksys.socialMediaApi.services.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {

    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;

    @Override
    public boolean validateUsername(String username) { return userRepository.existsByCredentialsIgnoreCaseUsername(username); }

    @Override
    public boolean validateHashtag(String label) {
        return hashtagRepository.existsByLabel(label);
    }

	@Override
	public boolean validateUsernameAvailable(String username) {
		return userRepository.existsByCredentialsIgnoreCaseUsername(username);
	}

}
