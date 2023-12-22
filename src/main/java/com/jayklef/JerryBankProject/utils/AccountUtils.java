package com.jayklef.JerryBankProject.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "002";
    public static final String ACCOUNT_EXISTS_MESSAGE = "CUSTOMER HAS AN ACCOUNT";
    public static final String ACCOUNT_CREATED_SUCCESSFULLY_CODE = "001";
    public static final String ACCOUNT_CREATED_SUCCESSFULLY_MESSAGE = "Account created successfully";

    public static String generateAccountNumber(){

        Year currentYear = Year.now();

        int min = 100000;
        int max = 999999;

        int random = (int)Math.floor(Math.random() * (max - min + 1) + min);

        // convert currentYear and randomNumber to string, then concatenate

        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(random);

        StringBuilder accountNumber = new StringBuilder();
        return accountNumber.append(year).append(randomNumber).toString();

    }

}
