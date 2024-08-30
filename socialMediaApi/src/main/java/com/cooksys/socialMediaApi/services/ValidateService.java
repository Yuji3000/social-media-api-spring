package com.cooksys.socialMediaApi.services;

public interface ValidateService {

    boolean validateHashtag(String label);

    boolean validateUsername(String username);

	boolean validateUsernameAvailable(String username);
}
