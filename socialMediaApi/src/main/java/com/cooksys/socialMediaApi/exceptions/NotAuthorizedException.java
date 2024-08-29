package com.cooksys.socialMediaApi.exceptions;

import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotAuthorizedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6823859620640891821L;

    private String message;
}
