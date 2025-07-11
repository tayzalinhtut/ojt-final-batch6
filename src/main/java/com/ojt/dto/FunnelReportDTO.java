package com.ojt.dto;

import lombok.Data;

@Data
public class FunnelReportDTO {
    int cvReceived;
    int scanPassCv;
    int codeTestPassCv;
    int interviewPassCv;
    int offerAcceptCv;
}