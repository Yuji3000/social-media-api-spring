package com.cooksys.socialMediaApi.exceptions;

import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ConflictException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5493791507446517907L;

    private String message;
}
