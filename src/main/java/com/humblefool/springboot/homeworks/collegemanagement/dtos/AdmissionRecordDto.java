package com.humblefool.springboot.homeworks.collegemanagement.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionRecordDto {
    private Long id;

    @NotNull(message = "Fees is required")
    @PositiveOrZero(message = "Fees cannot be negative")
    private Integer fees;

    private StudentDto student;
}