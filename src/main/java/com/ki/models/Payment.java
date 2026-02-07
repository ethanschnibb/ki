package com.ki.models;

import com.ki.Config;

import java.math.BigDecimal;
import java.time.LocalDate;


public class Payment {

    private int customerId;
    private LocalDate date;
    private int amount;
    private int fee;
    public Card card;

    public Payment() {
    }

    public Payment(String[] data) {
        this.setCustomerId(Integer.parseInt(data[0]));

        BigDecimal paymentFeeRate = Config.getPaymentFeeRate();
        int totalAmount = Integer.parseInt(data[2]);
        this.setFee(paymentFeeRate.multiply(new BigDecimal(totalAmount)).intValue());
        this.setAmount(totalAmount - this.getFee());
        this.setDate(LocalDate.parse(data[1]));

        Card card = new Card();
        card.setCardId(Integer.parseInt(data[3]));
        card.setStatus(data[4]);

        this.card = card;
    }

    public boolean isSuccessful() {
        return card.getStatus().equals("processed");
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }
}
