package com.ki.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ki.Config;

public class Card extends Payment {
    private int cardId;
    private String status;

    public Card(String[] data) {

        this.setCustomerId(Integer.parseInt(data[0]));

        BigDecimal paymentFeeRate = Config.getPaymentFeeRate();
        int totalAmount = Integer.parseInt(data[2]);

        this.setFee(paymentFeeRate.multiply(new BigDecimal(totalAmount)).intValue());
        this.setAmount(totalAmount - this.getFee());
        this.setDate(LocalDate.parse(data[1]));

        this.cardId = Integer.parseInt(data[3]);
        this.status = data[4];
    }

    @Override
    public boolean isSuccessful() {
        return "processed".equals(status);
    }

    public int getCardId() { 
        return cardId; 
    } 

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getStatus() { 
        return status; 
    }

    public void setStatus(String status) {
        this.status = status;
    } 
}
