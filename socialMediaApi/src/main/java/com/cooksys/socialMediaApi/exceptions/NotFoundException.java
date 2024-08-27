package com.cooksys.socialMediaApi.exceptions;

import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -5132315611984799553L;

    private String message;
}
