package com.ojt.service;

import com.ojt.dto.OJTDTO;
import com.ojt.entity.CV;
import com.ojt.enumeration.StatusType;

import java.util.List;

public interface EmailService {

    List<OJTDTO> getEligibleRecipients(StatusType cvStatusType, StatusType emailStatusType);

    void sendEmailToSelectedRecipients(List<String> recipientEmails, String subject, String body,
                                       StatusType emailStatusType, String senderName);

}