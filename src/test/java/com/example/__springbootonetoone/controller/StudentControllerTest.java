package com.example.__springbootonetoone.controller;


import com.example.__springbootonetoone.model.Student;
import com.example.__springbootonetoone.service.StudentImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentImpl studentService;

    @InjectMocks
    private StudentController studentController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddStudent_Success() throws Exception {
        // Given
        Student student = new Student();
        student.setName("John Doe");
        student.setAge(20);

        // When & Then
        mockMvc.perform(post("/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student))) // Fixed issue here
                .andExpect(status().isOk())
                .andExpect(content().string("Student Data Added"));

        verify(studentService, times(1)).addData(any(Student.class));
    }

    @Test
    void testUpdateStudent_Success() throws Exception {
        // Given
        long studentId = 1L;
        Student student = new Student();
        student.setName("Jane Doe");
        student.setAge(22);

        // When & Then
        mockMvc.perform(put("/update/{id}", studentId)  // Changed from post() to put()
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk());

        // Verify that service.update() was called with correct parameters
        verify(studentService, times(1)).update(eq(studentId), any(Student.class));
    }

    @Test
    void testGetStudentById_Success() throws Exception {
        // Given
        long studentId = 2L;
        Student student = new Student();
        student.setId(studentId);
        student.setName("John Doe");
        student.setAge(20);

        when(studentService.getStudentById(studentId)).thenReturn(student);

        // When & Then
        mockMvc.perform(get("/get/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(studentId))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value(20));

        verify(studentService, times(1)).getStudentById(studentId);
    }

    @Test
    void testGetStudents_Success() throws Exception {
        // Given
        int page = 0;
        int size = 2;
        List<Student> studentList = List.of(
                new Student(1, "John Doe", 20), // Corrected duplicate ID issue
                new Student(2, "Jane Doe", 22)
        );

        Page<Student> studentPage = new PageImpl<>(studentList, PageRequest.of(page, size), studentList.size());

        when(studentService.displayStudentData(page, size)).thenReturn(studentPage);

        // When & Then
        mockMvc.perform(get("/displayData")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(studentList.size())) // Dynamic check
                .andExpect(jsonPath("$.content[0].id").value(studentList.get(0).getId()))
                .andExpect(jsonPath("$.content[0].name").value(studentList.get(0).getName()))
                .andExpect(jsonPath("$.content[0].age").value(studentList.get(0).getAge()))
                .andExpect(jsonPath("$.content[1].id").value(studentList.get(1).getId()))
                .andExpect(jsonPath("$.content[1].name").value(studentList.get(1).getName()))
                .andExpect(jsonPath("$.content[1].age").value(studentList.get(1).getAge()));

        verify(studentService, times(1)).displayStudentData(page, size);
    }

    @Test
    void testFindByAge_EmptyList() throws Exception {
        // Given
        int age = 25; // No students with this age
        when(studentService.findByAge(age)).thenReturn(List.of());

        // When & Then
        mockMvc.perform(get("/findbyage/{age}", age)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())  // Ensure the response is an array
                .andExpect(jsonPath("$.length()").value(0)); // Ensure it's empty

        verify(studentService, times(1)).findByAge(age);
    }

    @Test
    void testDeleteStudent_Success() throws Exception {
        // Given
        long studentId = 1L;
        doNothing().when(studentService).deleteDataById(studentId);

        // When & Then
        mockMvc.perform(delete("/delete/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify that deleteDataById was called exactly once
        verify(studentService, times(1)).deleteDataById(studentId);
    }

}
