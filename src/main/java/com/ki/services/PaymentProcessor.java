package com.ki.services;

import com.ki.models.Payment;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

public class PaymentProcessor {

    public Payment[] getPayments(String csvPath, String source) {

        try {
            validateCsvHeader(csvPath, source);
        } catch (IOException e) {
            throw new RuntimeException("Failed to validate CSV file: ", e);
        }

        if ("card".equalsIgnoreCase(source)) {
            return parsePayments(csvPath, line -> line);
        } else if ("bank".equalsIgnoreCase(source)) {
            return parsePayments(csvPath, line -> adaptBankLine(line));
        } else {
            throw new IllegalArgumentException("Unsupported payment source: " + source);
        }
    }

    // Functional interface allows us to reuse the same parsing logic for both card and bank payments, just with different adapters for the CSV format
    // Takes a String[] as input and returns String[]
    private Payment[] parsePayments(String csvPath, Function<String[], String[]> adapter) {

        ArrayList<Payment> payments = new ArrayList<>();

        try (FileReader file = new FileReader(csvPath);
             CSVReader reader = new CSVReaderBuilder(file).withSkipLines(1).build()) {

            String[] line;

            while ((line = reader.readNext()) != null) {

                String[] adaptedLine = adapter.apply(line);

                Payment payment = new Payment(adaptedLine);
                payments.add(payment);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return payments.toArray(new Payment[0]);
    }

    private String[] adaptBankLine(String[] line) {

        // bank CSV format is different from card CSV format, need to adapt it to reuse the same Payment method:
        // customer_id,date,amount,bank_account_id

        return new String[]{
                line[0],    // customer_id
                line[1],    // date
                line[2],    // amount
                line[3],    // bank_account_id (reusing card_id position)
                "processed" // assume bank transfers always successful - allows us to reuse verification logic from cards
        };
    }

    private void validateCsvHeader(String csvPath, String source) throws IOException {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(csvPath)).build()) {
            String[] header = reader.readNext();
            if (header == null) {
                throw new IllegalArgumentException("CSV file is empty: " + csvPath);
            }
    
            if ("card".equalsIgnoreCase(source)) {
                if (header.length < 5 || !header[3].equalsIgnoreCase("card_id")) {
                    throw new IllegalArgumentException("CSV does not match expected card format.");
                }
            } else if ("bank".equalsIgnoreCase(source)) {
                if (header.length < 4 || !header[3].equalsIgnoreCase("bank_account_id")) {
                    throw new IllegalArgumentException("CSV does not match expected bank format.");
                }
            }
        }
    }

    public Payment[] verifyPayments(Payment[] payments) {

        ArrayList<Payment> filtered = new ArrayList<>();
        for (Payment payment : payments) {
            if (payment.isSuccessful()) {
                filtered.add(payment);
            }
        }

        return filtered.toArray(new Payment[]{});
    }
}
