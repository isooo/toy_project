package com.example.demo.exchangeRate.domain;

public class Money {
    private final double value;

    public Money(final double value) {
        if (value < 0) {
            throw new IllegalArgumentException();
        }
        this.value = Math.round(value * 100) / 100.0;;
    }

    public double getValue() {
        return value;
    }
}
