package com.ki.models;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BankPaymentTest {

    @Test
    public void testBankPaymentFromCsvRow() {
        int CUSTOMER_ID = 123;
        String AMOUNT = "2000";
        int BANK_ACCOUNT_ID = 45;
        String DATE = "2019-02-01";

        String data[] = new String[] {
                String.valueOf(CUSTOMER_ID),
                DATE,
                AMOUNT,
                String.valueOf(BANK_ACCOUNT_ID),
        };

        Bank payment = new Bank(data);

        assertEquals(CUSTOMER_ID, payment.getCustomerId());
        assertEquals(1960, payment.getAmount());
        assertEquals(40, payment.getFee());
        assertEquals(LocalDate.of(2019, 2, 1), payment.getDate());

        Bank bankPayment = (Bank) payment;
        assertEquals(BANK_ACCOUNT_ID, bankPayment.getBankAccountId());
    }
}
