package com.ki.services.adapters;

import com.ki.models.Card;
import com.ki.models.Payment;

/**
 * CardPaymentAdapter converts card payment CSV rows into Payment objects.
 *
 * Expected CSV format:
 * customer_id,date,amount,card_id,card_status
 *
 * Design notes:
 * - Card CSV format already matches Payment constructor expectations.
 * - Adapter acts mainly as a pass-through but maintains consistency
 *   with the adapter pattern, allowing future extension without
 *   modifying core processing logic.
 */
public class CardPaymentAdapter implements PaymentAdapter {

    /**
     * Converts card CSV row directly into Payment domain object.
     */
    @Override
    public Payment adapt(String[] line) {

        // No transformation required because card CSV aligns with Payment schema
        return new Card(line);
    }
}