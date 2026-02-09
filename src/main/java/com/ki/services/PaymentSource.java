package com.ki.services;

public enum PaymentSource {
    CARD,
    BANK;

    public static PaymentSource fromString(String source) {
        try {
            return PaymentSource.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported payment source: " + source);
        }
    }
}