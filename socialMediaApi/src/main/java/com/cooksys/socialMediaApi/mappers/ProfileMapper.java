package com.cooksys.socialMediaApi.mappers;

import com.cooksys.socialMediaApi.dtos.ProfileDto;
import com.cooksys.socialMediaApi.entities.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileDto entityToDto(Profile entity);

    Profile requestDtoToEntity(ProfileDto profileDto);

}
