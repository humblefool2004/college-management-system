package com.humblefool.springboot.homeworks.collegemanagement.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDto {
    private Long id;

    @NotBlank(message = "Subject title is required")
    private String title;

    private ProfessorDto professor;
}