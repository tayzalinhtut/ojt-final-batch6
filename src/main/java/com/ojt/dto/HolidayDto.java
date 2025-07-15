package com.ojt.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class HolidayDto {
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
