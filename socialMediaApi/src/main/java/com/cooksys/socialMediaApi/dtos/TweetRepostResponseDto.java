package com.cooksys.socialMediaApi.dtos;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetRepostResponseDto {

	private Long id;

	private UserResponseDto author;

	private Timestamp posted;

	private String content;


}
