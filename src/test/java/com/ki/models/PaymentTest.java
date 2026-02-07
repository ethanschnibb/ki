package com.ki.models;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class PaymentTest {

    @Test
    public void testPaymentFromCsvRow() {

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

        Payment payment = new Payment(data);

        assertEquals(CUSTOMER_ID, payment.getCustomerId());
        assertEquals(1960, payment.getAmount());
        assertEquals(40, payment.getFee());
        assertEquals(LocalDate.of(2019, 2, 1), payment.getDate());

        assertTrue(payment.card instanceof Card);

        Card card = payment.card;
        assertEquals(CARD_ID, card.getCardId());
        assertEquals(CARD_STATUS, card.getStatus());
    }

    @Test
    public void testIsSuccessful() {
        Payment payment = new Payment();
        payment.card = new Card();
        payment.card.setStatus("processed");
        assertTrue(payment.isSuccessful());
    }

    @Test
    public void testIsSuccessfulDeclined() {
        Payment payment = new Payment();
        payment.card = new Card();
        payment.card.setStatus("declined");
        assertFalse(payment.isSuccessful());
    }

    @Test
    public void testIsSuccessfulErrored() {
        Payment payment = new Payment();
        payment.card = new Card();
        payment.card.setStatus("error");
        assertFalse(payment.isSuccessful());
    }
}
