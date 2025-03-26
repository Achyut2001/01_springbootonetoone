package com.example.__springbootonetoone.controller;


import com.example.__springbootonetoone.model.Student;
import com.example.__springbootonetoone.service.StudentImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
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
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(content().string("Student Data Added"));

        verify(studentService, times(1)).addData(any(Student.class));
    }

    @Test
    void testUpdateStudent_Success() throws Exception {
        // Given
        long studentId = 1;
        Student updatedStudent = new Student();
        updatedStudent.setName("Jane Doe");
        updatedStudent.setAge(22);

        // Mock the service update method
        Mockito.doNothing().when(studentService).update(eq(studentId), any(Student.class));

        // When & Then
        mockMvc.perform(put("/update/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStudent)))
                .andExpect(status().isOk());


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
        // Corrected duplicate ID issue
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1, "John Doe", 20));
        studentList.add(new Student(2, "Jane Doe", 22));

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
    void testFindByAge() throws Exception {
        // Given
        int age = 20;
        List<Student> students = List.of(
                new Student(1, "John Doe", age),
                new Student(2, "Jane Doe", age)
        );

        // Mock the service findByAge method
        Mockito.when(studentService.findByAge(age)).thenReturn(students);

        // When & Then
        mockMvc.perform(get("/findByAge/{age}", age)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(students.size()))
                .andExpect(jsonPath("$[0].id").value(students.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(students.get(0).getName()))
                .andExpect(jsonPath("$[0].age").value(students.get(0).getAge()))
                .andExpect(jsonPath("$[1].id").value(students.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(students.get(1).getName()))
                .andExpect(jsonPath("$[1].age").value(students.get(1).getAge()));

        // Verify that the service findByAge method was called once with the correct parameter
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
