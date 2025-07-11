package com.ojt.dto;

import java.util.List;

import lombok.Data;

@Data
public class CustomAttendanceDTO {
    private int studentId;
    private String studentName;
    private List<String> attendance;
}