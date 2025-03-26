package com.example.__springbootonetoone.controller;

import com.example.__springbootonetoone.controller.customexception.StudentNotFound;
import org.hibernate.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    //Student Didn't Find Exception
    @ExceptionHandler(StudentNotFound.class)
    public ResponseEntity<?> handleStudentNotFoundException(StudentNotFound ex) {
        // You can customize the response as per your need
        return new ResponseEntity<>(new Error(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    // Handle NullPointerException
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, String>> handleNullPointerException(NullPointerException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Null pointer exception occurred");
        errors.put("message", ex.getMessage());  // Optionally include the message from the exception
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR); // Return 500 status code
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Map<String, String>> objectNotFoundException(ObjectNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Null pointer exception occurred");
        errors.put("message", ex.getMessage());  // Optionally include the message from the exception
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR); // Return 500 status code
    }


    //Parent Exception For Any Exception occurs
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex) {
        return "Something is incorrect: " + ex.getMessage();
    }

}
