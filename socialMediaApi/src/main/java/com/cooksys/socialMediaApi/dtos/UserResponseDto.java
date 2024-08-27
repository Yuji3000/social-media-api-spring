package com.cooksys.socialMediaApi.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class UserResponseDto {

    private String username;

    private ProfileDto profile;

    private Timestamp joined;

}
