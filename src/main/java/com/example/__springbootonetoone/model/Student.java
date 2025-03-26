package com.example.__springbootonetoone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Set;
@Entity
@Scope(value = "singleton")
@Component
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;
    @NotNull
    private Integer age;

    @OneToMany(cascade = CascadeType.ALL)
    //  @JsonManagedReference
    @JsonIgnore
    private Set<StudentSubject> subjects;


    public Student() {
    }

    public Student(Long id, String name, Integer age, Set<StudentSubject> subjects) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.subjects = subjects;
    }

    public Student(int i, String janeDoe, int i1) {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public @NotNull Integer getAge() {
        return age;
    }

    public void setAge(@NotNull Integer age) {
        this.age = age;
    }

    public Set<StudentSubject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<StudentSubject> subjects) {
        this.subjects = subjects;
    }
}
