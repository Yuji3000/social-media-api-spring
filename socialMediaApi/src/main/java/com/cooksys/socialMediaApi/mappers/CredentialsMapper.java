package com.cooksys.socialMediaApi.mappers;

import com.cooksys.socialMediaApi.dtos.CredentialsDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {
    CredentialsDto entityToDto(Credentials entity);

    Credentials RequestDtoToEntity(CredentialsDto credentialsDto);
}
