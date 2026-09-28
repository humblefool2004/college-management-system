package com.humblefool.springboot.homeworks.collegemanagement.services;

import com.humblefool.springboot.homeworks.collegemanagement.dtos.AdmissionRecordDto;
import com.humblefool.springboot.homeworks.collegemanagement.dtos.ProfessorDto;
import com.humblefool.springboot.homeworks.collegemanagement.dtos.StudentDto;
import com.humblefool.springboot.homeworks.collegemanagement.dtos.SubjectDto;
import com.humblefool.springboot.homeworks.collegemanagement.entities.AdmissionRecord;
import com.humblefool.springboot.homeworks.collegemanagement.entities.Professor;
import com.humblefool.springboot.homeworks.collegemanagement.entities.Student;
import com.humblefool.springboot.homeworks.collegemanagement.entities.Subject;
import com.humblefool.springboot.homeworks.collegemanagement.exceptions.DuplicateResourceException;
import com.humblefool.springboot.homeworks.collegemanagement.exceptions.ResourceNotFoundException;
import com.humblefool.springboot.homeworks.collegemanagement.repositories.AdmissionRecordRepository;
import com.humblefool.springboot.homeworks.collegemanagement.repositories.ProfessorRepository;
import com.humblefool.springboot.homeworks.collegemanagement.repositories.StudentRepository;
import com.humblefool.springboot.homeworks.collegemanagement.repositories.SubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollegeService {
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final SubjectRepository subjectRepository;
    private final AdmissionRecordRepository admissionRecordRepository;

    // ---------- lookup helpers ----------

    private Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    private Professor findProfessor(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found with id: " + id));
    }

    private Subject findSubject(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + id));
    }

    // ---------- relationships ----------

    // Student owns the student_professor join table, so we update the Student side
    @Transactional
    public StudentDto assignStudentToProfessor(Long studentId, Long professorId) {
        Student student = findStudent(studentId);
        Professor professor = findProfessor(professorId);

        student.getProfessors().add(professor);
        Student saved = studentRepository.save(student);

        log.info("Linked student {} to professor {}", studentId, professorId);
        return new StudentDto(saved.getId(), saved.getName());
    }

    // Subject owns the subject_student join table, so we update the Subject side
    @Transactional
    public void assignSubjectToStudent(Long subjectId, Long studentId) {
        Student student = findStudent(studentId);
        Subject subject = findSubject(subjectId);

        subject.getStudents().add(student);
        subjectRepository.save(subject);

        log.info("Linked subject {} to student {}", subjectId, studentId);
    }

    // ---------- create ----------

    @Transactional
    public StudentDto createStudent(StudentDto studentDto) {
        Student student = new Student();
        student.setName(studentDto.getName());
        Student saved = studentRepository.save(student);

        log.info("Created student with id {}", saved.getId());
        return new StudentDto(saved.getId(), saved.getName());
    }

    @Transactional
    public ProfessorDto createProfessor(ProfessorDto professorDto) {
        Professor professor = new Professor();
        professor.setName(professorDto.getName());
        Professor saved = professorRepository.save(professor);

        log.info("Created professor with id {}", saved.getId());
        return new ProfessorDto(saved.getId(), saved.getName());
    }

    @Transactional
    public SubjectDto createSubject(SubjectDto subjectDto) {
        Subject subject = new Subject();
        subject.setTitle(subjectDto.getTitle());
        Subject saved = subjectRepository.save(subject);

        log.info("Created subject with id {}", saved.getId());
        return new SubjectDto(saved.getId(), saved.getTitle(), null);
    }

    @Transactional
    public AdmissionRecordDto createAdmissionRecord(Long studentId, AdmissionRecordDto dto) {
        Student student = findStudent(studentId);

        if (admissionRecordRepository.existsByStudentId(studentId)) {
            throw new DuplicateResourceException("Student with id " + studentId + " already has an admission record");
        }

        AdmissionRecord record = new AdmissionRecord();
        record.setFees(dto.getFees());
        record.setStudent(student);
        AdmissionRecord saved = admissionRecordRepository.save(record);

        log.info("Created admission record {} for student {}", saved.getId(), studentId);
        StudentDto studentDto = new StudentDto(student.getId(), student.getName());
        return new AdmissionRecordDto(saved.getId(), saved.getFees(), studentDto);
    }

    // ---------- read ----------

    @Transactional(readOnly = true)
    public StudentDto getStudentById(Long studentId) {
        Student student = findStudent(studentId);
        return new StudentDto(student.getId(), student.getName());
    }

    @Transactional(readOnly = true)
    public ProfessorDto getProfessorById(Long professorId) {
        Professor professor = findProfessor(professorId);
        return new ProfessorDto(professor.getId(), professor.getName());
    }

    @Transactional(readOnly = true)
    public SubjectDto getSubjectById(Long subjectId) {
        Subject subject = findSubject(subjectId);
        ProfessorDto professorDto = null;
        if (subject.getProfessor() != null) {
            professorDto = new ProfessorDto(subject.getProfessor().getId(), subject.getProfessor().getName());
        }
        return new SubjectDto(subject.getId(), subject.getTitle(), professorDto);
    }

    // ---------- delete ----------

    @Transactional
    public void deleteStudent(Long studentId) {
        Student student = findStudent(studentId);

        // Subject owns subject_student, so Hibernate only cleans those join rows
        // if we remove the student from each subject's collection first.
        // (student_professor is owned by Student, so Hibernate cleans it automatically.
        // The admission record is removed by cascade = REMOVE.)
        for (Subject subject : student.getSubjects()) {
            subject.getStudents().remove(student);
        }

        studentRepository.delete(student);
        log.info("Deleted student {}", studentId);
    }

    @Transactional
    public void deleteProfessor(Long professorId) {
        Professor professor = findProfessor(professorId);

        // Subject owns the professor_id FK: null it out so no FK violation occurs
        for (Subject subject : professor.getSubjects()) {
            subject.setProfessor(null);
        }

        // Student owns student_professor: remove the professor from each student's set
        for (Student student : professor.getStudents()) {
            student.getProfessors().remove(professor);
        }

        professorRepository.delete(professor);
        log.info("Deleted professor {}", professorId);
    }

    @Transactional
    public void deleteSubject(Long subjectId) {
        Subject subject = findSubject(subjectId);

        // Subject owns subject_student, so its join rows are removed automatically
        subjectRepository.delete(subject);
        log.info("Deleted subject {}", subjectId);
    }
}