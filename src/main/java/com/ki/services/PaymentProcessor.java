package com.ki.services;

import com.ki.models.Payment;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.ki.services.adapters.PaymentAdapter;
import com.ki.services.adapters.CardPaymentAdapter;
import com.ki.services.adapters.BankPaymentAdapter;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PaymentProcessor is responsible for:
 *
 * 1) Loading payment records from a CSV file
 * 2) Validating input structure based on payment source
 * 3) Delegating parsing logic to a PaymentAdapter strategy
 * 4) Returning domain Payment objects for further processing
 *
 * Design notes:
 * - Adapter pattern isolates CSV format differences between sources.
 * - Enum PaymentSource prevents stringly-typed logic.
 * - Public API remains unchanged to preserve compatibility with upstream platform modules.
 */
public class PaymentProcessor {

    private static final Logger logger = LoggerFactory.getLogger(PaymentProcessor.class);

    /**
     * Entry point for loading payments.
     *
     * @param csvPath path to CSV input file
     * @param source payment source ("card", "bank") — converted internally to enum
     *
     * Responsibilities:
     * - Validate source input
     * - Validate CSV header structure
     * - Select appropriate parsing strategy
     * - Delegate file processing
     */
    public Payment[] getPayments(String csvPath, String source) {

        // Convert external string input into domain-safe enum
        PaymentSource paymentSource = PaymentSource.fromString(source);

        // Strategy selection:
        // Adapter encapsulates source-specific parsing rules.
        PaymentAdapter adapter;

        switch (paymentSource) {
            case CARD:
                adapter = new CardPaymentAdapter();
                break;
            case BANK:
                adapter = new BankPaymentAdapter();
                break;
            default:
                logger.error("Unsupported payment source: {}", source);
                throw new IllegalArgumentException("Unsupported source");
        }
    
        // Stream CSV rows and convert into Payment domain objects
        return parsePayments(csvPath, adapter, paymentSource);
    }

    /**
     * Reads CSV file line-by-line and converts rows into Payment objects.
     *
     * Design notes:
     * - Streaming approach avoids loading entire CSV into memory.
     * - Adapter abstracts differences in CSV structure.
     * - Try-with-resources ensures file handles are closed safely.
     */
    private Payment[] parsePayments(String csvPath, PaymentAdapter adapter, PaymentSource source) {

        ArrayList<Payment> payments = new ArrayList<>();

        try (FileReader file = new FileReader(csvPath);
             CSVReader reader = new CSVReaderBuilder(file).build()) {

            String[] line = reader.readNext();

            validateCsvHeader(line, source);
            
            // Process each row sequentially
            while ((line = reader.readNext()) != null) {

                // Adapter converts raw CSV fields into domain Payment object
                Payment payment = adapter.adapt(line);
                payments.add(payment);
            }

        } catch (IOException e) {
            logger.error("Failed to parse payments from CSV file: {}", csvPath, e);
            throw new RuntimeException(
                "Failed to parse payments from CSV file: " + csvPath,
                e
            );
        }

        logger.info("Successfully parsed {} payments from {}", payments.size(), csvPath);
        return payments.toArray(new Payment[0]);
    }

    /**
     * Validates CSV header structure before processing.
     *
     * This prevents:
     * - mismatched CSV files
     * - incorrect source selection
     * - downstream parsing errors
     *
     * Fail-fast validation improves debuggability.
     */
    private void validateCsvHeader(String[] header, PaymentSource source) throws IOException {
    
        if (header == null) {
            logger.error("CSV file is empty");
            throw new IllegalArgumentException("CSV file is empty");
        }

        switch (source) {
            // Card CSV expected format:
            // customer_id,date,amount,card_id,card_status
            case CARD:
                if (header.length < 5 || !header[3].equalsIgnoreCase("card_id")) {
                    logger.error("CSV header does not match expected card format.");
                    throw new IllegalArgumentException("CSV does not match expected card format.");
                }
                break;
                
            // Bank CSV expected format:
            // customer_id,date,amount,bank_account_id
            case BANK:
                if (header.length < 4 || !header[3].equalsIgnoreCase("bank_account_id")) {
                    logger.error("CSV does not match expected bank format.");
                    throw new IllegalArgumentException("CSV does not match expected bank format.");
                }
                break;
        }
    }

    /**
     * Filters payments to include only successful transactions.
     *
     * Note:
     * - Business rule encapsulated here rather than parsing stage.
     * - Keeps parsing logic pure and separation of concerns clear.
     */
    public Payment[] verifyPayments(Payment[] payments) {

        ArrayList<Payment> filtered = new ArrayList<>();
        for (Payment payment : payments) {
            if (payment.isSuccessful()) {
                filtered.add(payment);
            }
        }

        logger.info("Filtered {} successful payments out of {}", filtered.size(), payments.length);
        return filtered.toArray(new Payment[0]);
    }
}
