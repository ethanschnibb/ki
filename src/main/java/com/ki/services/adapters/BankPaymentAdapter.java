package com.ki.services.adapters;

import com.ki.models.Payment;

/**
 * BankPaymentAdapter converts bank transfer CSV rows into Payment objects.
 *
 * Expected bank CSV format:
 * customer_id,date,amount,bank_account_id
 *
 * Differences from card CSV:
 * - No card_status column.
 * - Bank transfers are assumed successful by business rule.
 *
 * Implementation detail:
 * - Adapter reshapes bank CSV into equivalent structure expected
 *   by Payment constructor so downstream logic remains unchanged.
 *
 * Architectural benefit:
 * - Keeps Payment model stable while allowing multiple external formats.
 */
public class BankPaymentAdapter implements PaymentAdapter {

    /**
     * Adapts bank CSV row into card-like structure used by Payment constructor.
     */
    @Override
    public Payment adapt(String[] line) {

        String[] adaptedLine = new String[]{
            line[0],   // customer_id
            line[1],   // date
            line[2],   // amount
            line[3],   // bank_account_id reused as card_id position
            "processed" // business rule: bank transfers always successful
        };

        return new Payment(adaptedLine);
    }
}