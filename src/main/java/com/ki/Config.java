package com.ki;

import java.math.BigDecimal;

public class Config {

    public static BigDecimal getPaymentFeeRate() {
        return new BigDecimal("0.02");
    }
}
