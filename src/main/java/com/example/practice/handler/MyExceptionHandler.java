package com.example.practice.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@ControllerAdvice
public class MyExceptionHandler {
    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> notFoundHandler(MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>("Illegal argument! An integer is expected.", HttpStatus.NOT_FOUND);
    }

}
