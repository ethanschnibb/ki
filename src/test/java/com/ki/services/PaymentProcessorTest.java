package com.ki.services;

import com.ki.models.Card;
import com.ki.Fixture;
import com.ki.models.Payment;
import org.junit.Test;

import static org.junit.Assert.*;

public class PaymentProcessorTest {

    // ------------------- CARD TESTS -------------------

    @Test
    public void testGetPayments() {
        String fixturePath = Fixture.getPath("card_payments_mixed.csv");

        PaymentProcessor processor = new PaymentProcessor();
        Payment[] payments = processor.getPayments(fixturePath, "card");

        assertEquals(3, payments.length);

        assertEquals(30, payments[0].card.getCardId());
        assertEquals(45, payments[1].card.getCardId());
        assertEquals(10, payments[2].card.getCardId());
    }

    @Test
    public void testGetPaymentsEmpty() {
        String fixturePath = Fixture.getPath("card_payments_empty.csv");

        PaymentProcessor processor = new PaymentProcessor();
        Payment[] payments = processor.getPayments(fixturePath, "card");

        assertEquals(0, payments.length);
    }

    @Test
    public void testVerifyPayments() {
        Payment payment1 = createPayment("processed");
        Payment payment2 = createPayment("declined");
        Payment payment3 = createPayment("processed");

        Payment[] payments = {payment1, payment2, payment3};

        PaymentProcessor processor = new PaymentProcessor();
        Payment[] result = processor.verifyPayments(payments);

        Payment[] expected = {payment1, payment3};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testCardCsvFormatIncorrect() {
        String fixturePath = Fixture.getPath("card_payments_wrong_format.csv");

        PaymentProcessor processor = new PaymentProcessor();

        try {
            processor.getPayments(fixturePath, "card");
            fail("Expected IllegalArgumentException for wrong card CSV format");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("CSV does not match expected card format"));
        }
    }

    private Payment createPayment(String cardStatus) {
        Card card = new Card();
        card.setStatus(cardStatus);
        Payment payment = new Payment();
        payment.card = card;
        return payment;
    }

    // ------------------- BANK TESTS -------------------

    @Test
    public void testGetBankPayments() {
        String fixturePath = Fixture.getPath("bank_payments_mixed.csv");

        PaymentProcessor processor = new PaymentProcessor();
        Payment[] payments = processor.getPayments(fixturePath, "bank");

        assertEquals(2, payments.length);

        // bank_account_id is mapped to cardId internally
        assertEquals(20, payments[0].card.getCardId());
        assertEquals(60, payments[1].card.getCardId());

        // all bank payments are marked processed
        assertTrue(payments[0].isSuccessful());
        assertTrue(payments[1].isSuccessful());
    }

    @Test
    public void testVerifyBankPayments() {
        String fixturePath = Fixture.getPath("bank_payments_mixed.csv");

        PaymentProcessor processor = new PaymentProcessor();
        Payment[] payments = processor.getPayments(fixturePath, "bank");

        Payment[] filtered = processor.verifyPayments(payments);

        // all bank payments should pass
        assertEquals(payments.length, filtered.length);
    }

    @Test
    public void testBankCsvFormatIncorrect() {
        String fixturePath = Fixture.getPath("bank_payments_wrong_format.csv");

        PaymentProcessor processor = new PaymentProcessor();

        try {
            processor.getPayments(fixturePath, "bank");
            fail("Expected IllegalArgumentException for wrong bank CSV format");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("CSV does not match expected bank format"));
        }
    }

    // ------------------- EDGE CASES -------------------

    @Test
    public void testGetPaymentsEmptyCsv() {
        String fixturePath = Fixture.getPath("null_payments.csv");

        PaymentProcessor processor = new PaymentProcessor();

        try {
            processor.getPayments(fixturePath, "card");
            fail("Expected IllegalArgumentException for empty CSV");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("CSV file is empty"));
        }
    }

    @Test
    public void testUnsupportedPaymentSource() {
        String fixturePath = Fixture.getPath("card_payments_mixed.csv");

        PaymentProcessor processor = new PaymentProcessor();

        try {
            processor.getPayments(fixturePath, "unsupported_source");
            fail("Expected IllegalArgumentException for unsupported payment source");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unsupported payment source"));
        }
    }

    @Test
    public void shouldThrowRuntimeExceptionWhenCsvCannotBeRead() {

        PaymentProcessor processor = new PaymentProcessor();

        String invalidPath = "non-existent-file.csv";

        RuntimeException ex = assertThrows(
            RuntimeException.class,
            () -> processor.getPayments(invalidPath, "card")
        );

        assertTrue(ex.getMessage().contains("Failed to validate CSV file"));

        assertNotNull(ex.getCause());
    }
}