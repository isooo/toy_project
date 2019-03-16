package com.example.demo.exchangeRate.domain;

import java.math.BigDecimal;

public class ExchangeRate {
    private final Country baseCurrency;
    private final Country quotedCurrency;
    private final double rate;

    public ExchangeRate(Country baseCurrency, Country quotedCurrency, double rate) {
        this.baseCurrency = baseCurrency;
        this.quotedCurrency = quotedCurrency;
        this.rate = rate;
    }

    public double getRate() {
        return rate;
    }

    public Money exchange(final Money money) {
        return new Money(
                new BigDecimal(this.rate)
                        .multiply(new BigDecimal(money.getValue()))
                        .doubleValue()
        );
    }

    @Override
    public String toString() {
        return "ExchangeRate[" +
                "baseCurrency='" + baseCurrency + '\'' +
                ", quotedCurrency='" + quotedCurrency + '\'' +
                ", rate=" + rate +
                ']';
    }
}
