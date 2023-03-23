package com.demo.springjwt.exception;

import com.demo.springjwt.modal.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandling {

    public static final String EMAIL_NOT_FOUND = "Email was not found";
    public static final String EMAIL_EXIST = "Email was not found";

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException emailNotFounException){
        return createHttpResponse(HttpStatus.BAD_REQUEST,
                EMAIL_NOT_FOUND.toUpperCase());
    }

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<HttpResponse> emailExistException(EmailNotFoundException emailNotFounException){
        return createHttpResponse(HttpStatus.BAD_REQUEST,
                EMAIL_EXIST.toUpperCase());
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message){
        HttpResponse httpResponse = new HttpResponse(LocalDateTime.now(), httpStatus.value(), httpStatus,
                httpStatus.getReasonPhrase().toUpperCase() , message);
        return new ResponseEntity<>(httpResponse, httpStatus);
    }
}
