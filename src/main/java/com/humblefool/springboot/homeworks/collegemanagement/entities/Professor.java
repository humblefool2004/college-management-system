package com.humblefool.springboot.homeworks.collegemanagement.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // inverse side: the FK (professor_id) lives in the Subject table
    @OneToMany(mappedBy = "professor", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<Subject> subjects = new HashSet<>();

    // inverse side: Student owns the student_professor join table
    @ManyToMany(mappedBy = "professors", fetch = FetchType.LAZY)
    private Set<Student> students = new HashSet<>();
}