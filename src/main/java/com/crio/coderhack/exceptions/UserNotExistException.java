package com.crio.coderhack.exceptions;

public class UserNotExistException extends RuntimeException {
    public UserNotExistException(String message){
        super(message);
    }
}
