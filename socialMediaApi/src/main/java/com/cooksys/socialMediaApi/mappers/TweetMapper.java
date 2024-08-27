package com.cooksys.socialMediaApi.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.socialMediaApi.dtos.TweetRequestDto;
import com.cooksys.socialMediaApi.dtos.TweetResponseDto;
import com.cooksys.socialMediaApi.entities.Tweet;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TweetMapper {

	TweetResponseDto entityToDto(Tweet entity);

	Tweet requestDtoToEntity(TweetRequestDto tweetRequestDto);

	List<TweetResponseDto> entitiesToDtos(List<Tweet> entities);

}
