package com.jayklef.JerryBankProject.service.Impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jayklef.JerryBankProject.model.Customer;
import com.jayklef.JerryBankProject.model.Transaction;
import com.jayklef.JerryBankProject.repository.CustomerRepository;
import com.jayklef.JerryBankProject.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {

    private TransactionRepository transactionRepository;
    private CustomerRepository customerRepository;
    private static final String FILE = "C:\\Users\\Admin\\Documents\\MyStatement.pdf";

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        List<Transaction> transactions = transactionRepository.findAll()
                .stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt().isEqual(start))
                .filter(transaction -> transaction.getCreatedAt().isEqual(end))
                .toList();

        Customer customer = customerRepository.findByAccountNumber(accountNumber);
        String customerName = customer.getFirstname() + " "+ customer.getLastname()
                +" "+ customer.getMiddleName();


        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        log.info("Setting document size");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        // create a table for bank information

        PdfPTable bankInfoTable = new PdfPTable(1);

        // set the bank name

        PdfPCell bankName = new PdfPCell(new Phrase("Jerry Bank"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.GRAY);
        bankName.setPadding(20f);

        // set the bank address

        PdfPCell bankAddress = new PdfPCell(new Phrase("7 Abel Abayomi Street, Ajah, Lagos"));
        bankAddress.setBorder(0);

        // add bankName and bankAddress to Table

        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);


        // create table for statement information
        PdfPTable statementInfoTable = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start date " + startDate));
        customerInfo.setBorder(0);

        PdfPCell acctStatement = new PdfPCell(new Phrase("ACCOUNT STATEMENT"));
        acctStatement.setBorder(0);

        PdfPCell statementEndDate = new PdfPCell(new Phrase("End date: " + endDate));
        statementEndDate.setBorder(0);

        PdfPCell name = new PdfPCell(new Phrase("Customer name " + customerName));
        name.setBorder(0);

        // give space

        PdfPCell space = new PdfPCell();
        space.setBorder(0);

        // get customer address

        PdfPCell address = new PdfPCell(new Phrase("Customer Address "+ customer.getAddress()));
        address.setBorder(0);


        // create table to record transactions
        PdfPTable transactionsTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("Date"));
        date.setBackgroundColor(BaseColor.BLUE);
        date.setBorder(0);

        PdfPCell description = new PdfPCell(new Phrase("Description"));
        description.setBackgroundColor(BaseColor.BLUE);
        description.setBorder(0);

        PdfPCell amount = new PdfPCell(new Phrase("Amount"));
        amount.setBackgroundColor(BaseColor.BLUE);
        amount.setBorder(0);

        PdfPCell status = new PdfPCell(new Phrase("Status"));
        status.setBackgroundColor(BaseColor.BLUE);
        status.setBorder(0);

        transactionsTable.addCell(date);
        transactionsTable.addCell(description);
        transactionsTable.addCell(amount);
        transactionsTable.addCell(status);

        transactions.forEach(transaction -> {
            transactionsTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
            transactionsTable.addCell(new Phrase(transaction.getTransactionType()));
            transactionsTable.addCell(new Phrase(transaction.getAmount().toString()));
            transactionsTable.addCell(new Phrase(transaction.getStatus()));
        });


        statementInfoTable.addCell(customerInfo);
        statementInfoTable.addCell(acctStatement);
        statementInfoTable.addCell(statementEndDate);
        statementInfoTable.addCell(name);
        statementInfoTable.addCell(space);
        statementInfoTable.addCell(address);

        // add tables into document

        document.add(bankInfoTable);
        document.add(statementInfoTable);
        document.add(transactionsTable);

        document.close();

        return transactions;
    }

  /*  private void designStatement(List<Transaction> transactions) throws FileNotFoundException, DocumentException {
        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        log.info("Setting document size");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        // create a table for bank information

        PdfPTable bankInfoTable = new PdfPTable(1);

        // set the bank name

        PdfPCell bankName = new PdfPCell(new Phrase("Jerry Bank"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.GRAY);
        bankName.setPadding(20f);

        // set the bank address

        PdfPCell bankAddress = new PdfPCell(new Phrase("7 Abel Abayomi Street, Ajah, Lagos"));
        bankAddress.setBorder(0);

        // add bankName and bankAddress to Table

        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);


        // create table for statement information
        PdfPTable statementInfoTable = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start date " + startDate));
        customerInfo.setBorder(0);

        PdfPCell acctStatement = new PdfPCell(new Phrase("ACCOUNT STATEMENT"));
        acctStatement.setBorder(0);

        PdfPCell statementEndDate = new PdfPCell(new Phrase("End date", endDate));
        statementEndDate.setBorder(0);

        PdfPCell name = new PdfPCell(new Phrase("Customer name", customerName));
        name.setBorder(0);

        // give space

        PdfPCell space = new PdfPCell();
        space.setBorder(0);

        // get customer address

        PdfPCell address = new PdfPCell(new Phrase("Customer Address", customer.getAddress));
        address.setBorder(0);


        // create table to record transactions
        PdfPTable transactionsTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("Date"));
        date.setBackgroundColor(BaseColor.BLUE);
        date.setBorder(0);

        PdfPCell description = new PdfPCell(new Phrase("Description"));
        description.setBackgroundColor(BaseColor.BLUE);
        description.setBorder(0);

        PdfPCell amount = new PdfPCell(new Phrase("Amount"));
        amount.setBackgroundColor(BaseColor.BLUE);
        amount.setBorder(0);

        PdfPCell status = new PdfPCell(new Phrase("Status"));
        status.setBackgroundColor(BaseColor.BLUE);
        status.setBorder(0);

        transactionsTable.addCell(date);
        transactionsTable.addCell(description);
        transactionsTable.addCell(amount);
        transactionsTable.addCell(status);

        transactions.forEach(transaction -> {
            transactionsTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
            transactionsTable.addCell(new Phrase(transaction.getTransactionType()));
            transactionsTable.addCell(new Phrase(transaction.getAmount().toString()));
            transactionsTable.addCell(new Phrase(transaction.getStatus()));
        });


        statementInfoTable.addCell(customerInfo);
        statementInfoTable.addCell(acctStatement);
        statementInfoTable.addCell(statementEndDate);
        statementInfoTable.addCell(name);
        statementInfoTable.addCell(space);
        statementInfoTable.addCell(address);

        // add tables into document

        document.add(bankInfoTable);
        document.add(statementInfoTable);
        document.add(transactionsTable);

        document.close();
    }     */
}
