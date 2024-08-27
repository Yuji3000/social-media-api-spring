package com.cooksys.socialMediaApi.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class TweetRequestDto {

    private UserResponseDto author;

    private Timestamp posted;

    private String content;

    private TweetResponseDto inReplyTo;

    private TweetResponseDto repostOf;

}
