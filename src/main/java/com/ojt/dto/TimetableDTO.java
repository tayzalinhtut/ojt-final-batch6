package com.ojt.dto;

import com.ojt.enumeration.DayOfWeek;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class  TimetableDTO {

    private Long id;

    @NotNull(message = "time is required")
    private String time;
    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;
    @NotNull(message = "Course is required")
    private Long courseId;
}
