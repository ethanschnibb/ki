package com.ki.services.adapters;

import com.ki.models.Payment;

/**
 * PaymentAdapter defines a strategy interface for converting
 * raw CSV input rows into domain Payment objects.
 *
 * Design rationale:
 * - Different payment sources provide CSV files with different schemas.
 * - Rather than branching logic throughout the parsing pipeline,
 *   each source implements its own adapter responsible for translation.
 *
 * Benefits:
 * - Open/Closed Principle: new payment types can be added without modifying existing logic.
 * - Separation of concerns: CSV structure knowledge is isolated here.
 * - Improves testability and readability.
 */
public interface PaymentAdapter {

    /**
     * Converts a raw CSV row into a Payment domain object.
     *
     * @param line raw CSV row represented as string array
     * @return mapped Payment object
     */
    Payment adapt(String[] line);
}