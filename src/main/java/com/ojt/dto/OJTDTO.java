package com.ojt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.ojt.entity.Batch;
import com.ojt.entity.CV;
import com.ojt.entity.Status;
import com.ojt.enumeration.StatusType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OJTDTO {

    private Long id;

    @Size(min = 1, message = "Bank account is required")
    private String bankAccount;

    private Long statusId;

    private Long cvId;

    private Long batchId;

    @NotBlank(message = "Name is required")
    private String name;

    private String email;

    @NotBlank(message = "Phone number is required")
    private String phone;

    private String batchName;

    @NotNull(message = "Status name is required")
    private StatusType statusName;
}