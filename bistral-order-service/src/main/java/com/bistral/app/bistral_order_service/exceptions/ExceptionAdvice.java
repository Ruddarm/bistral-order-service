package com.bistral.app.bistral_order_service.exceptions;


import com.bistral.app.bistral_order_service.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> handelValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach((error) -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ErrorResponse> handelResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().errorMsg(ex.getMessage()).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handelGeneralException(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(ErrorResponse.builder().errorMsg(ex.getMessage()).build(), HttpStatus.BAD_REQUEST);
    }
}
