package com.humblefool.springboot.homeworks.collegemanagement.repositories;


import com.humblefool.springboot.homeworks.collegemanagement.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
