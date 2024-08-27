package com.cooksys.socialMediaApi.mappers;

import com.cooksys.socialMediaApi.dtos.CredentialsDto;
import com.cooksys.socialMediaApi.entities.Credentials;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {
    CredentialsDto entityToDto(Credentials entity);

    Credentials requestDtoToEntity(CredentialsDto credentialsDto);
}
