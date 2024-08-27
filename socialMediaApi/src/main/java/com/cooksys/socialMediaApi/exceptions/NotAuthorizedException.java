package com.cooksys.socialMediaApi.exceptions;

import java.io.Serial;

public class NotAuthorizedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6823859620640891821L;

    private String message;
}
