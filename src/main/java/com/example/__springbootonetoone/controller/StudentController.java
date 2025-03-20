package com.example.__springbootonetoone.controller;

import com.example.__springbootonetoone.model.Student;
import com.example.__springbootonetoone.service.StudentImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studentportal")
public class StudentController {

    @Autowired
    private StudentImpl service;

    @Transactional
    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")//it is optional
    public ResponseEntity<String> addData(@Valid @RequestBody Student student) {

        service.addData(student);
        return ResponseEntity.ok("Student added");
    }

    @GetMapping("/get/{id}")
    public Student getDataById(@PathVariable Long id) {
        return service.getStudentById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteDataByID(@PathVariable Long id) {
        service.deleteDataById(id);
    }

    @GetMapping("displayData")
    public ResponseEntity<Page<Student>> getStudents(
            @RequestParam int page,
            @RequestParam int size) {
        Page<Student> students = service.displayStudentData(page, size);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/findbyage/{age}")
    public List<Student> findByAge(@PathVariable int age) {
        return service.findByAge(age);
    }

}



