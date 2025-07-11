package com.ojt.dto;


import com.ojt.entity.OJT;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OJTCustomDTO {
    private OJTDTO ojt;
    private double attendance;
}