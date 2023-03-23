package com.demo.springjwt.exception;

public class EmailExistException extends Exception{

    public EmailExistException(String message){
        super(message);
    }
}
