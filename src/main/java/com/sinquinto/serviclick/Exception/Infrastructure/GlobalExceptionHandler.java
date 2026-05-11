package com.sinquinto.serviclick.Exception.Infrastructure;

import com.sinquinto.serviclick.Exception.Domain.ErrorDetails;
import com.sinquinto.serviclick.Exception.Domain.ResourceDuplicateException;
import com.sinquinto.serviclick.Exception.Domain.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request){
        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                "NOT FOUND");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceDuplicateException.class)
    public ResponseEntity<ErrorDetails> handleResourceDuplicateException(ResourceDuplicateException ex, WebRequest request){
        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                "DUPLICATE"
        );
        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
    }
}
