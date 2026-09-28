package com.humblefool.springboot.homeworks.collegemanagement.repositories;

import com.humblefool.springboot.homeworks.collegemanagement.entities.AdmissionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionRecordRepository extends JpaRepository<AdmissionRecord, Long> {

    // derived query: checks the student.id column of AdmissionRecord
    boolean existsByStudentId(Long studentId);
}