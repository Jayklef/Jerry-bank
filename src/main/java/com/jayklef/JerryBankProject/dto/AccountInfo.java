package com.jayklef.JerryBankProject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo {

    private String accountName;
    private String accountNumber;
    private String accountBalance;
}