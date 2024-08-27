package com.cooksys.socialMediaApi.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class UserRequestDto {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

}
