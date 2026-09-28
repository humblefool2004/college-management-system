package com.humblefool.springboot.homeworks.collegemanagement.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorDto {
    private Long id;

    @NotBlank(message = "Professor name is required")
    private String name;
}