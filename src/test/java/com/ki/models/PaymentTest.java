package com.ki.models;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class PaymentTest {

    @Test
    public void testCardPaymentFromCsvRow() {

        int CUSTOMER_ID = 123;
        String AMOUNT = "2000";
        String CARD_STATUS = "processed";
        int CARD_ID = 45;
        String DATE = "2019-02-01";

        String data[] = new String[] {
                String.valueOf(CUSTOMER_ID),
                DATE,
                AMOUNT,
                String.valueOf(CARD_ID),
                CARD_STATUS,
        };

        Payment payment = new Card(data);

        assertEquals(CUSTOMER_ID, payment.getCustomerId());
        assertEquals(1960, payment.getAmount());
        assertEquals(40, payment.getFee());
        assertEquals(LocalDate.of(2019, 2, 1), payment.getDate());

        // Downcast because base type is Payment
        assertTrue(payment instanceof Card);

        Card cardPayment = (Card) payment;
        assertEquals(CARD_ID, cardPayment.getCardId());
        assertEquals(CARD_STATUS, cardPayment.getStatus());
    }

    @Test
    public void testIsSuccessfulProcessed() {

        String[] data = {"123","2019-02-01","2000","45","processed"};

        Payment payment = new Card(data);

        assertTrue(payment.isSuccessful());
    }

    @Test
    public void testIsSuccessfulDeclined() {

        String[] data = {"123","2019-02-01","2000","45","declined"};

        Payment payment = new Card(data);

        assertFalse(payment.isSuccessful());
    }

    @Test
    public void testIsSuccessfulErrored() {

        String[] data = {"123","2019-02-01","2000","45","error"};

        Payment payment = new Card(data);

        assertFalse(payment.isSuccessful());
    }
}