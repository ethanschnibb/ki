package com.ki.services.adapters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import com.ki.models.Bank;
import com.ki.models.Payment;

public class BankPaymentAdapterTest {

    @Test
    public void adaptValidRowReturnsBank() {
        String[] line = new String[]{"123", "2021-01-02", "100", "900"};

        BankPaymentAdapter adapter = new BankPaymentAdapter();
        Payment payment = adapter.adapt(line);

        assertTrue(payment instanceof Bank);
        Bank bank = (Bank) payment;

        assertEquals(123, bank.getCustomerId());
        // totalAmount 100, fee = 2% -> 2, amount = 98
        assertEquals(98, bank.getAmount());
        assertEquals(2, bank.getFee());
        assertEquals(900, bank.getBankAccountId());
        assertTrue(bank.isSuccessful());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void adaptMissingColumnsThrows() {
        String[] line = new String[]{"1", "2021-01-02", "100"};
        BankPaymentAdapter adapter = new BankPaymentAdapter();
        adapter.adapt(line);
    }

    @Test(expected = NumberFormatException.class)
    public void adaptInvalidNumberThrows() {
        String[] line = new String[]{"1", "2021-01-02", "100", "not-int"};
        BankPaymentAdapter adapter = new BankPaymentAdapter();
        adapter.adapt(line);
    }
}
