package com.example.__springbootonetoone.service;

import com.example.__springbootonetoone.controller.customexception.StudentNotFound;
import com.example.__springbootonetoone.model.Student;
import com.example.__springbootonetoone.repositery.StudentRepo;
import com.example.__springbootonetoone.repositery.StudentSubjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class StudentImpl {

    @Autowired
    private StudentRepo service;
    @Autowired
    private StudentSubjectRepo subjectRepo;


    public Student addData(Student student) {
        return service.save(student);
    }

    public Page<Student> displayStudentData(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return service.findAll(pageable);
    }


    public Student getStudentById(long id) {
        return service.findById(id)
                .orElseThrow(() -> new StudentNotFound("Id Is Not Present"));
    }


    public void update(long id, Student student) {
        Student st = service.findById(id)
                .orElseThrow(() -> new StudentNotFound("Id Is Not Present"));
        st.setName(student.getName());
        st.setSubjects(student.getSubjects());
        st.setAge(student.getAge());
        service.save(st);
    }

    public void deleteDataById(long id) {
        service.deleteById(id);
    }

    public List<Student> findByAge(int age) {
        return service.findByAgeLessThan(age);
    }

}
