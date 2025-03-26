package com.example.__springbootonetoone.RepositeryTest.serviceTest;

import com.example.__springbootonetoone.controller.customexception.StudentNotFound;
import com.example.__springbootonetoone.model.Student;
import com.example.__springbootonetoone.repositery.StudentRepo;
import com.example.__springbootonetoone.service.StudentImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StudentImplTest {

    @Mock
    private StudentRepo studentRepo;

    @InjectMocks
    private StudentImpl studentService;

    private Student student;
    private Student student1;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        student = new Student();
        student.setId(1L);
        student.setName("John Doe");
        student.setAge(20);

        student1 = new Student();
        student1.setId(2L); // Different ID
        student1.setName("Jane Doe"); // Fixed name
        student1.setAge(18);
    }

    @Test
    public void AddDataTest() {
        // Given
        when(studentRepo.save(any(Student.class))).thenReturn(student);

        // When
        Student result = studentService.addData(student);

        // Then
        verify(studentRepo, times(1)).save(student);
        assertNotNull(result);
        assertEquals(student.getName(), result.getName());
    }

    @Test
    public void DeleteDataById_SuccessTest() {
        // Given
        doNothing().when(studentRepo).deleteById(anyLong());

        // When
        studentService.deleteDataById(1L);

        // Then
        verify(studentRepo, times(1)).deleteById(1L);
    }

    @Test
    public void DeleteDataById_NotFoundTest() {
        // Given
        doThrow(new RuntimeException("Student not found")).when(studentRepo).deleteById(anyLong());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            studentService.deleteDataById(1L);
        });

        assertEquals("Student not found", exception.getMessage());
    }

    @Test
    public void FindByAge_StudentsFoundTest() {
        // Given
        when(studentRepo.findByAgeLessThan(20)).thenReturn(Arrays.asList(student1, student));

        // When
        List<Student> result = studentService.findByAge(20);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(student1));
        assertTrue(result.contains(student));
    }

    @Test
    public void FindByAge_NoStudentsFoundTest() {
        // Given
        when(studentRepo.findByAgeLessThan(18)).thenReturn(Arrays.asList());

        // When
        List<Student> result = studentService.findByAge(18);

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void FindByAge_SingleStudentFoundTest() {
        // Given
        when(studentRepo.findByAgeLessThan(21)).thenReturn(Arrays.asList(student1));

        // When
        List<Student> result = studentService.findByAge(21);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(student1));
    }

    @Test
    void GetStudentById_SuccessTest() {
        // Given
        when(studentRepo.findById(1L)).thenReturn(Optional.of(student));

        // When
        Student foundStudent = studentService.getStudentById(1L);

        // Then
        assertNotNull(foundStudent);
        assertEquals(1L, foundStudent.getId());
        assertEquals("John Doe", foundStudent.getName());

        verify(studentRepo, times(1)).findById(1L);
    }

    @Test
    void GetStudentById_ThrowsExceptionTest() {
        // Given
        when(studentRepo.findById(2L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(StudentNotFound.class, () -> {
            studentService.getStudentById(2L);
        });

        assertTrue(exception.getMessage().contains("ID IS NOT PRESENT"));
        verify(studentRepo, times(1)).findById(2L);
    }

    @Test
    public void DisplayStudentDataTest() {
        // Given
        int page = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(page, size);

        List<Student> students = Arrays.asList(student1, student);
        Page<Student> studentPage = new PageImpl<>(students, pageable, students.size());

        when(studentRepo.findAll(pageable)).thenReturn(studentPage);

        // When
        Page<Student> result = studentService.displayStudentData(page, size);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("Jane Doe", result.getContent().get(0).getName()); // Fixed expected name
        assertEquals("John Doe", result.getContent().get(1).getName());

        verify(studentRepo, times(1)).findAll(pageable);
    }

    private Student existingStudent;
    private Student updatedStudent;

    @BeforeEach
    void setup2() {
        MockitoAnnotations.openMocks(this);

        // Existing student in the database
        existingStudent = new Student();
        existingStudent.setId(1L);
        existingStudent.setName("John Doe");
        existingStudent.setAge(20);

        // New student data for update
        updatedStudent = new Student();
        updatedStudent.setName("Jane Doe");
        updatedStudent.setAge(22);
    }

    @Test
    void testUpdateStudent_Success() {
        // Given: Mocking studentRepo to return an existing student
        when(studentRepo.findById(1L)).thenReturn(Optional.of(existingStudent));

        // When: Updating student
        studentService.update(1L, updatedStudent);

        // Then: Verify that the student details are updated
        assertEquals("Jane Doe", existingStudent.getName());
        assertEquals(22, existingStudent.getAge());

        // Verify save() is called
        verify(studentRepo, times(1)).save(existingStudent);
    }

}
