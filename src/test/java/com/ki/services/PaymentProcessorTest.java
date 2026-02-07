package com.ki.services;

import com.ki.models.Card;
import org.junit.Test;
import static org.junit.Assert.*;

import com.ki.Fixture;
import com.ki.models.Payment;

public class PaymentProcessorTest {

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

    private Payment createPayment(String cardStatus) {
        Card card = new Card();
        card.setStatus(cardStatus);
        Payment payment = new Payment();
        payment.card = card;
        return payment;
    }
}
