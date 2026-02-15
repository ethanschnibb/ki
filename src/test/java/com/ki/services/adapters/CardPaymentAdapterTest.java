package com.ki.services.adapters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import com.ki.models.Card;
import com.ki.models.Payment;

public class CardPaymentAdapterTest {

    @Test
    public void adaptValidRowReturnsCard() {
        String[] line = new String[]{"123", "2021-01-02", "100", "42", "processed"};

        CardPaymentAdapter adapter = new CardPaymentAdapter();
        Payment payment = adapter.adapt(line);

        assertTrue(payment instanceof Card);
        Card card = (Card) payment;

        assertEquals(123, card.getCustomerId());
        // totalAmount 100, fee = 2% -> 2, amount = 98
        assertEquals(98, card.getAmount());
        assertEquals(2, card.getFee());
        assertEquals(42, card.getCardId());
        assertTrue(card.isSuccessful());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void adaptMissingColumnsThrows() {
        // too few columns
        String[] line = new String[]{"1", "2021-01-02", "100"};
        CardPaymentAdapter adapter = new CardPaymentAdapter();
        adapter.adapt(line);
    }

    @Test(expected = NumberFormatException.class)
    public void adaptInvalidNumberThrows() {
        String[] line = new String[]{"abc", "2021-01-02", "not-a-number", "x", "processed"};
        CardPaymentAdapter adapter = new CardPaymentAdapter();
        adapter.adapt(line);
    }
}
