package com.example.demo.exchangeRate.domain;

public class Money {
    private final double value;

    public Money(final double value) {
        if (value < 0) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
