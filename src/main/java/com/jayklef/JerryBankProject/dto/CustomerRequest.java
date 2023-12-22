package com.jayklef.JerryBankProject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {
    private String firstname;
    private String lastname;
    private String middleName;
    private String email;
    private String gender;
    private String telephone;
    private String alternateNumber;
    private String address;
    private String state;
    private String status;
}
