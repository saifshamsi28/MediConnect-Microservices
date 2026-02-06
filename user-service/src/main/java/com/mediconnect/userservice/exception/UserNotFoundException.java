package com.mediconnect.userservice.exception;

public class UserNotFoundException extends RuntimeException{
    UserNotFoundException(String message){
        super(message);
    }
}
