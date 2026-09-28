package com.humblefool.springboot.homeworks.collegemanagement.repositories;

import com.humblefool.springboot.homeworks.collegemanagement.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
