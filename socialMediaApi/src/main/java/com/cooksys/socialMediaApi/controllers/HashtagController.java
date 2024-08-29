package com.cooksys.socialMediaApi.controllers;

import com.cooksys.socialMediaApi.entities.Hashtag;
import com.cooksys.socialMediaApi.services.HashtagService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class HashtagController {

    private final HashtagService hashtagService;

    @GetMapping
    public List<Hashtag> getTags() {
        return hashtagService.getTags();
    }
}
