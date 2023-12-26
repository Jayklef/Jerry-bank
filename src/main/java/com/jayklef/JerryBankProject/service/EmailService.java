package com.jayklef.JerryBankProject.service;

import com.jayklef.JerryBankProject.dto.EmailDetails;

public interface EmailService {

    void sendEmailAlerts(EmailDetails emailDetails);
    void sendEmailWithAttachment(EmailDetails emailDetails);
}
