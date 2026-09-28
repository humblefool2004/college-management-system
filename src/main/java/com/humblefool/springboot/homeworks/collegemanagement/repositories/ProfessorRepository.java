package com.humblefool.springboot.homeworks.collegemanagement.repositories;

import com.humblefool.springboot.homeworks.collegemanagement.entities.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}
