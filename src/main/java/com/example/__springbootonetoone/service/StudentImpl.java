package com.example.__springbootonetoone.service;

import com.example.__springbootonetoone.model.Student;
import com.example.__springbootonetoone.repositery.StudentRepo;
import com.example.__springbootonetoone.repositery.studentSubjectRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;


@Service
@Validated
public class StudentImpl {

    @Autowired
    private StudentRepo service;

    @Autowired
    private studentSubjectRepo subjectRepo;

    public void addData(@Valid Student student) {
        service.save(student);
        //subjectRepo.save(subject);
    }

    public Page<Student> displayStudentData(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        return service.findAll(pageable);
    }

    public List<Student> displaySubjectData() {
        return this.service.findAll();
    }


    public void updateStudentData(long id) {


    }

    public Student getStudentById(long id) {
        Optional<Student> student = service.findById(id);
        return student.orElse(null);  // Returns a student if found, otherwise returns null
    }

    public void deleteDataById(long id) {
        service.deleteById(id);
    }

    public List<Student> findByAge(int age) {
        return (List<Student>) service.findByAgeLessThan(age);
    }

}
