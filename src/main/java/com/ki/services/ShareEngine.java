package com.ki.services;

import com.ki.models.Payment;
import com.ki.models.ShareOrder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShareEngine {

    private static final Logger logger = LoggerFactory.getLogger(ShareEngine.class);
    
    public ShareOrder[] generateShareOrders(BigDecimal sharePrice, Payment[] payments) {
        logger.info("Generating share orders for {} payments at share price {}", payments.length, sharePrice);

        ArrayList<ShareOrder> shareOrders = new ArrayList<>();

        Map<String, List<Payment>> paymentsByCustomer = groupPaymentsByCustomer(payments);

        for (Map.Entry<String, List<Payment>> entry : paymentsByCustomer.entrySet()) {
            String customerId = entry.getKey();

            List<Payment> customerPayments = entry.getValue();

            int totalShares = 0;
            for (Payment payment : customerPayments) {
                int shares = new BigDecimal(payment.getAmount()).divideToIntegralValue(sharePrice).intValue();
                totalShares += shares;
            }

            ShareOrder shareOrder = new ShareOrder();
            shareOrder.setCustomerId(Integer.parseInt(customerId));
            shareOrder.setShares(totalShares);
            shareOrders.add(shareOrder);

        }

        logger.info("Finished generating {} share orders", shareOrders.size());
        return shareOrders.toArray(new ShareOrder[shareOrders.size()]);
    }

    private Map<String, List<Payment>> groupPaymentsByCustomer(Payment[] payments) {
        Map<String, List<Payment>> paymentsByCustomer = new HashMap<>();

        for (Payment payment : payments) {
            String customerId = String.valueOf(payment.getCustomerId());
            if (!paymentsByCustomer.containsKey(customerId)) {
                paymentsByCustomer.put(customerId, new ArrayList<Payment>());
            }
            paymentsByCustomer.get(customerId).add(payment);
        }

        return paymentsByCustomer;
    }
}
