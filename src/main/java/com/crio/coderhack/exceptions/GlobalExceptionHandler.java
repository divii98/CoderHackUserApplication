package com.crio.coderhack.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserAlreadyExistException.class})
    public ResponseEntity<Object> handleUserAlreadyExistException(UserAlreadyExistException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new Error(ex.getMessage()));
    }

    @ExceptionHandler({UserNotExistException.class})
    public ResponseEntity<Object> handleUserNotExist(UserNotExistException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(ex.getMessage()));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error(ex.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleValidationException( MethodArgumentNotValidException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage()));
    }
}
