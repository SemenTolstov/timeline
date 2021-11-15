package com.timeline.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistException.class)
    public HttpEntity<ErrorResponse> handleUserAlreadyExistException(HttpServletRequest request,
                                                                     UserAlreadyExistException ex) {
        String errorDetails = "User already exist. Try different login";
        ErrorResponse error = new ErrorResponse("User already exist", Collections.singletonList(errorDetails));
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessErrorException.class)
    public HttpEntity<ErrorResponse> handleAccessErrorException(HttpServletRequest request,
                                                                AccessErrorException ex) {
        String errorDetails = "Access error. User with input uuid haven't such message";
        ErrorResponse error = new ErrorResponse("Access error", Collections.singletonList(errorDetails));
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public HttpEntity<ErrorResponse> handleUserNotFoundException(HttpServletRequest request,
                                                                 UserAlreadyExistException ex) {
        String errorDetails = "User not found. Check data for correct";
        ErrorResponse error = new ErrorResponse("User not found", Collections.singletonList(errorDetails));
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessageNotFoundException.class)
    public HttpEntity<ErrorResponse> handleMessageNotFoundException(HttpServletRequest request,
                                                                    MessageNotFoundException ex) {
        String errorDetails = "Message not found. Check data for correct";
        ErrorResponse error = new ErrorResponse("Message not found", Collections.singletonList(errorDetails));
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HttpEntity<ErrorResponse> handleMethodArgumentNotValidException(HttpServletRequest request,
                                                                           MethodArgumentNotValidException ex) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        ErrorResponse error = new ErrorResponse("Validation Failed", details);
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }
}
