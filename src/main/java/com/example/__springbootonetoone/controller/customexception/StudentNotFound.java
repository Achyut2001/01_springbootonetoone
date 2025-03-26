package com.example.__springbootonetoone.controller.customexception;

public class StudentNotFound extends RuntimeException {

    public StudentNotFound(String mess) {
        super(" STUDENT WITH THIS ID IS NOT PRESENT");
    }
}
