package com.cooksys.socialMediaApi.controllers;

import com.cooksys.socialMediaApi.services.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/validate")
public class ValidateController {

    private final ValidateService validateService;

    @GetMapping("/username/exists/@{username}")
    public boolean validateUsername(@PathVariable String username) {
        return validateService.validateUsername(username);
    }

    @GetMapping("/tag/exists/{label}")
    public boolean validateHashtag(@PathVariable String label) { return validateService.validateHashtag(label); }

    @GetMapping("/username/available/@{username}")
    public boolean validateUsernameAvailable(@PathVariable String username) { 
    	return !validateService.validateUsernameAvailable(username);}
}
