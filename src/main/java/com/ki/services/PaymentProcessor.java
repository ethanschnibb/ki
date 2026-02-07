package com.ki.services;

import com.ki.models.Payment;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PaymentProcessor {

    public Payment[] getPayments(String csvPath, String source) {

        ArrayList<Payment> payments = new ArrayList<>();

        String[] line;
        try {
            FileReader file = new FileReader(csvPath);
            CSVReader reader = new CSVReaderBuilder(file).withSkipLines(1).build();
            while (true) {
                if (!((line = reader.readNext()) != null)) {
                    break;
                }

                Payment payment = new Payment(line);
                payments.add(payment);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return payments.toArray(new Payment[]{});
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
