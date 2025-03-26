package com.example.__springbootonetoone.repositery;

import com.example.__springbootonetoone.model.Student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {
    Page<Student> findAll(Pageable pageable);

    public List<Student> findByAgeLessThan(Integer age);

}