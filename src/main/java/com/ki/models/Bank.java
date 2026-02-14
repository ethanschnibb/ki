package com.ki.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ki.Config;

public class Bank extends Payment {

    private int bankAccountId;

    public Bank(String[] data) {

        this.setCustomerId(Integer.parseInt(data[0]));

        BigDecimal paymentFeeRate = Config.getPaymentFeeRate();
        int totalAmount = Integer.parseInt(data[2]);

        this.setFee(paymentFeeRate.multiply(new BigDecimal(totalAmount)).intValue());
        this.setAmount(totalAmount - this.getFee());
        this.setDate(LocalDate.parse(data[1]));

        this.bankAccountId = Integer.parseInt(data[3]);
    }

    @Override
    public boolean isSuccessful() {
        return true;
    }

    public int getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(int bankAccountId) {
        this.bankAccountId = bankAccountId;
    }
}
