package com.ojt.enumeration;

public enum StatusType {

    // For CV Status
    Scan_Pass,
    Scan_Fail,
    CodeTest_Pass, //
    CodeTest_Fail, //
    Interview_Pass,
    Interview_Fail,
    Offer_Accept,
    Offer_Reject,

    //For OJt Status
    OJT_Active,
    OJT_Withdraw,
    OJT_Pass,
    OJT_Fail,

    //For EMAIL Status
    Email_CodeTestInvite,
    Email_InterviewInvite,
    Email_InterviewResult,
    Email_CodeTestFail,
    Email_InterviewFail,

    //User Status
    Active,
    Inactive,

    //For Batch
    Batch_Active,
    Batch_Completed
}
