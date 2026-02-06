package com.mediconnect.userservice.exception;

public class UserAlreadyExistException extends RuntimeException{
    UserAlreadyExistException(String message){
        super(message);
    }
}
