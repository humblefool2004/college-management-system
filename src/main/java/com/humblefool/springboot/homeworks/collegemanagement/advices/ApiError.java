package com.humblefool.springboot.homeworks.collegemanagement.advices;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiError(
        LocalDateTime timestamp,
        int status,
        String message,
        Map<String, String> subErrors
) {
}