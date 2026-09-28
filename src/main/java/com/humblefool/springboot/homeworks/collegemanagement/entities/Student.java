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
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // owning side: Student owns the student_professor join table
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "student_professor",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "professor_id")
    )
    private Set<Professor> professors = new HashSet<>();

    // inverse side: Subject owns the subject_student join table
    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    private Set<Subject> subjects = new HashSet<>();

    // inverse side: AdmissionRecord holds the FK (student_id)
    @OneToOne(mappedBy = "student", cascade = CascadeType.REMOVE)
    private AdmissionRecord admissionRecord;
}