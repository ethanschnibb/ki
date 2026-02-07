package com.ki.services;

import org.junit.Test;
import static org.junit.Assert.*;

import com.ki.models.Payment;
import com.ki.models.ShareOrder;

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

        assertEquals(123, result[0].getCustomerId());
        assertEquals(3500, result[0].getShares());

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

    private Payment createPayment(int customerId, int amount) {
        Payment payment = new Payment();
        payment.setCustomerId(customerId);
        payment.setAmount(amount);
        return payment;
    }

}
