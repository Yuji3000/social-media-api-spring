package com.cooksys.socialMediaApi.mappers;

import com.cooksys.socialMediaApi.dtos.HashtagDto;
import com.cooksys.socialMediaApi.entities.Hashtag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

    Hashtag requestDtoToEntity(HashtagDto hashtagDto);

    HashtagDto entityToDto(Hashtag hashtag);

}
