package com.crio.coderhack.exceptions;

import com.crio.coderhack.constants.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleUserAlreadyExistException() {
        UserAlreadyExistException ex = new UserAlreadyExistException(Constants.EXIST_SAME_WITH_ID);

        ResponseEntity<Object> response = exceptionHandler.handleUserAlreadyExistException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(Constants.EXIST_SAME_WITH_ID,((Error) Objects.requireNonNull(response.getBody())).getError());
    }

    @Test
    void testHandleUserNotExistException() {
        UserNotExistException ex = new UserNotExistException(Constants.NOT_EXIST_WITH_GIVEN_ID);

        ResponseEntity<Object> response = exceptionHandler.handleUserNotExist(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Constants.NOT_EXIST_WITH_GIVEN_ID, ((Error) Objects.requireNonNull(response.getBody())).getError());
    }

    @Test
    void testHandleRuntimeException() {
        RuntimeException ex = new RuntimeException("Internal server error");

        ResponseEntity<Object> response = exceptionHandler.handleRuntimeException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", ((Error) Objects.requireNonNull(response.getBody())).getError());
    }

    @Test
    void testHandleValidationException() {
        FieldError fieldError = new FieldError("UpdateRequest", "score", "Score cannot be null");
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getFieldError()).thenReturn(fieldError);

        ResponseEntity<Object> response = exceptionHandler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Score cannot be null", ((Error) Objects.requireNonNull(response.getBody())).getError());
    }
}
