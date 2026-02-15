package com.ki.services;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

import com.ki.Fixture;
import com.ki.models.Bank;
import com.ki.models.Card;
import com.ki.models.Payment;

public class PaymentProcessorTest {

    // ------------------- CARD TESTS -------------------

    @Test
    public void testGetCardPayments() {

        String fixturePath = Fixture.getPath("card_payments_mixed.csv");

        PaymentProcessor processor = new PaymentProcessor();
        Payment[] payments = processor.getPayments(fixturePath, "card");

        assertEquals(3, payments.length);

        // Downcast to concrete type
        assertTrue(payments[0] instanceof Card);
        assertTrue(payments[1] instanceof Card);
        assertTrue(payments[2] instanceof Card);

        Card p1 = (Card) payments[0];
        Card p2 = (Card) payments[1];
        Card p3 = (Card) payments[2];

        assertEquals(30, p1.getCardId());
        assertEquals(45, p2.getCardId());
        assertEquals(10, p3.getCardId());
    }

    @Test
    public void testGetCardPaymentsEmpty() {

        String fixturePath = Fixture.getPath("card_payments_empty.csv");

        PaymentProcessor processor = new PaymentProcessor();
        Payment[] payments = processor.getPayments(fixturePath, "card");

        assertEquals(0, payments.length);
    }

    @Test
    public void testVerifyCardPayments() {

        Payment payment1 = createCardPayment("processed");
        Payment payment2 = createCardPayment("declined");
        Payment payment3 = createCardPayment("processed");

        Payment[] payments = {payment1, payment2, payment3};

        PaymentProcessor processor = new PaymentProcessor();
        Payment[] result = processor.verifyPayments(payments);

        assertArrayEquals(new Payment[]{payment1, payment3}, result);
    }

    @Test
    public void testCardCsvFormatIncorrect() {

        String fixturePath = Fixture.getPath("card_payments_wrong_format.csv");

        PaymentProcessor processor = new PaymentProcessor();

        try {
            processor.getPayments(fixturePath, "card");
            fail("Expected IllegalArgumentException for wrong card CSV format");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("card format"));
        }
    }

    private Payment createCardPayment(String status) {

        String[] data = {
                "123",
                "2019-02-01",
                "2000",
                "45",
                status
        };

        return new Card(data);
    }

    // ------------------- BANK TESTS -------------------

    @Test
    public void testGetBankPayments() {

        String fixturePath = Fixture.getPath("bank_payments_mixed.csv");

        PaymentProcessor processor = new PaymentProcessor();
        Payment[] payments = processor.getPayments(fixturePath, "bank");

        assertEquals(2, payments.length);

        assertTrue(payments[0] instanceof Bank);
        assertTrue(payments[1] instanceof Bank);

        // behaviour testing instead of internal fields
        assertTrue(payments[0].isSuccessful());
        assertTrue(payments[1].isSuccessful());
    }

    @Test
    public void testVerifyBankPayments() {

        String fixturePath = Fixture.getPath("bank_payments_mixed.csv");

        PaymentProcessor processor = new PaymentProcessor();
        Payment[] payments = processor.getPayments(fixturePath, "bank");

        Payment[] filtered = processor.verifyPayments(payments);

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
            assertTrue(e.getMessage().contains("bank format"));
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
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unsupported"));
        }
    }

    @Test
    public void shouldThrowRuntimeExceptionWhenCsvCannotBeRead() {

        PaymentProcessor processor = new PaymentProcessor();

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> processor.getPayments("non-existent-file.csv", "card")
        );

        assertTrue(ex.getMessage().contains("Failed to parse payments from CSV file"));
        assertNotNull(ex.getCause());
    }
}