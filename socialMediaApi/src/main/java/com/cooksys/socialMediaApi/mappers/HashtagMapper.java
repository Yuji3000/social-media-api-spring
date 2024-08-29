package com.cooksys.socialMediaApi.mappers;

import com.cooksys.socialMediaApi.dtos.HashtagResponseDto;
import com.cooksys.socialMediaApi.entities.Hashtag;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

    HashtagResponseDto entityToDto(Hashtag hashtag);

    List<HashtagResponseDto> entitiesToDtos(List<Hashtag> hashtags);

}
