package com.humblefool.springboot.homeworks.collegemanagement.controllers;

import com.humblefool.springboot.homeworks.collegemanagement.dtos.AdmissionRecordDto;
import com.humblefool.springboot.homeworks.collegemanagement.dtos.ProfessorDto;
import com.humblefool.springboot.homeworks.collegemanagement.dtos.StudentDto;
import com.humblefool.springboot.homeworks.collegemanagement.dtos.SubjectDto;
import com.humblefool.springboot.homeworks.collegemanagement.services.CollegeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final CollegeService collegeService;

    // Link a student to a professor
    @PutMapping("/{studentId}/professors/{professorId}")
    public ResponseEntity<StudentDto> assignProfessor(@PathVariable Long studentId, @PathVariable Long professorId) {
        return ResponseEntity.ok(collegeService.assignStudentToProfessor(studentId, professorId));
    }

    // Link a subject to a student
    @PutMapping("/{studentId}/subjects/{subjectId}")
    public ResponseEntity<Void> assignSubject(@PathVariable Long studentId, @PathVariable Long subjectId) {
        collegeService.assignSubjectToStudent(subjectId, studentId);
        return ResponseEntity.noContent().build();
    }

    // Create a base student
    @PostMapping
    public ResponseEntity<StudentDto> addStudent(@RequestBody @Valid StudentDto studentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(collegeService.createStudent(studentDto));
    }

    // Create a base professor
    @PostMapping("/professors")
    public ResponseEntity<ProfessorDto> createProfessor(@RequestBody @Valid ProfessorDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(collegeService.createProfessor(dto));
    }

    // Create a subject
    @PostMapping("/subjects")
    public ResponseEntity<SubjectDto> createSubject(@RequestBody @Valid SubjectDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(collegeService.createSubject(dto));
    }

    // Create an admission record for a student
    @PostMapping("/{studentId}/admission")
    public ResponseEntity<AdmissionRecordDto> createAdmission(@PathVariable Long studentId,
                                                              @RequestBody @Valid AdmissionRecordDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(collegeService.createAdmissionRecord(studentId, dto));
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentDto> getStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(collegeService.getStudentById(studentId));
    }

    @GetMapping("/professors/{professorId}")
    public ResponseEntity<ProfessorDto> getProfessor(@PathVariable Long professorId) {
        return ResponseEntity.ok(collegeService.getProfessorById(professorId));
    }

    @GetMapping("/subjects/{subjectId}")
    public ResponseEntity<SubjectDto> getSubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(collegeService.getSubjectById(subjectId));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long studentId) {
        collegeService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/professors/{professorId}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Long professorId) {
        collegeService.deleteProfessor(professorId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/subjects/{subjectId}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long subjectId) {
        collegeService.deleteSubject(subjectId);
        return ResponseEntity.noContent().build();
    }
}