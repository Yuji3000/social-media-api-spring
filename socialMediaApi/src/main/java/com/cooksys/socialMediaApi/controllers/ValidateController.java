package com.cooksys.socialMediaApi.controllers;

import com.cooksys.socialMediaApi.services.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/validate")
public class ValidateController {

    private final ValidateService validateService;
}
