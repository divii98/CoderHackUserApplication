package com.crio.coderhack.exceptions;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException( String message){
        super(message);
    }
}
