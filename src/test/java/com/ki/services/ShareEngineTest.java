package com.ki.services;

import org.junit.Test;
import static org.junit.Assert.*;

import com.ki.models.Payment;
import com.ki.models.ShareOrder;
import com.ki.models.Bank;

import java.math.BigDecimal;

public class ShareEngineTest {

    @Test
    public void testGenerateShareOrdersDifferentCustomers() {
        ShareEngine shareEngine = new ShareEngine();

        Payment[] payments = new Payment[] {
            createPayment(456, 900),
            createPayment(123, 4200),
        };

        ShareOrder[] result = shareEngine.generateShareOrders(new BigDecimal("1.2"), payments);

        assertEquals(2, result.length);

        // Check shares for customer 123
        assertEquals(123, result[0].getCustomerId());
        assertEquals(3500, result[0].getShares());

        // Check shares for customer 456
        assertEquals(456, result[1].getCustomerId());
        assertEquals(750, result[1].getShares());
    }

    @Test
    public void testGenerateShareOrdersSameCustomer() {
        ShareEngine shareEngine = new ShareEngine();

        int customerId = 456;

        Payment[] payments = new Payment[] {
            createPayment(customerId, 900),
            createPayment(customerId, 4200),
        };

        ShareOrder[] result = shareEngine.generateShareOrders(new BigDecimal("1.2"), payments);

        assertEquals(1, result.length);

        assertEquals(456, result[0].getCustomerId());
        assertEquals(4250, result[0].getShares());
    }

    // ---------------------- TEST HELPERS ----------------------

    /**
     * Creates a simple Payment subclass for testing that
     * returns exactly the amount and customerId we specify.
     */
    private Payment createPayment(int customerId, int amount) {
        return new TestPayment(customerId, amount);
    }

    /**
     * Minimal concrete Payment class for testing ShareEngine.
     * Bypasses fees and any CSV parsing logic.
     */
    private static class TestPayment extends Payment {

        public TestPayment(int customerId, int amount) {
            setCustomerId(customerId);
            setAmount(amount);
        }

        @Override
        public boolean isSuccessful() {
            return true;
        }
    }
}