package com.cooksys.socialMediaApi.controllers.advice;

import com.cooksys.socialMediaApi.dtos.ErrorDto;
import com.cooksys.socialMediaApi.exceptions.BadRequestException;
import com.cooksys.socialMediaApi.exceptions.NotAuthorizedException;
import com.cooksys.socialMediaApi.exceptions.ConflictException;
import com.cooksys.socialMediaApi.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "com.cooksys.socialMediaApi.controllers")
@ResponseBody
public class DefaultControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ErrorDto handleBadRequestException(HttpServletRequest request, BadRequestException badRequestException) {
        return new ErrorDto(badRequestException.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorDto handleNotFoundException(HttpServletRequest request, NotFoundException notFoundException) {
        return new ErrorDto(notFoundException.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NotAuthorizedException.class)
    public ErrorDto handleNotAuthorizedException(HttpServletRequest request,
                                                 NotAuthorizedException notAuthorizedException) {
        return new ErrorDto(notAuthorizedException.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ErrorDto handleConflictException(HttpServletRequest request, ConflictException conflictException) {
        return new ErrorDto(conflictException.getMessage());
    }
}
