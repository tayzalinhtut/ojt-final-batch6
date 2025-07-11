package com.ojt.dto;

import lombok.Data;

@Data
public class DropoffReportDTO {
    int scanFailCv;
    int codeTestFailCv;
    int interviewFailCv;
    int offerRejectCv;
}