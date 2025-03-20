package com.example.__springbootonetoone.repositery;

import com.example.__springbootonetoone.model.Student;
import com.example.__springbootonetoone.model.StudentSubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface studentSubjectRepo extends JpaRepository<StudentSubject,Long> {
    Page<StudentSubject> findAll(Pageable pageable);

}
