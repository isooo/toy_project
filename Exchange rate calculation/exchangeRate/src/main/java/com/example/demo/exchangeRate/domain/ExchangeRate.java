package com.example.demo.exchangeRate.domain;

import java.math.BigDecimal;

public class ExchangeRate {
    private final Country base;
    private final Country quotes;
    private final double rate;

    public ExchangeRate(
            final Country base,
            final Country quotes,
            final double rate
    ) {
        this.base = base;
        this.quotes = quotes;
        this.rate = rate;
    }

    public Money exchange(final Money money) {
        return new Money(
                new BigDecimal(this.rate)
                        .multiply(new BigDecimal(money.getValue()))
                        .doubleValue()
        );
    }

    public double getRate() {
        return rate;
    }

    public String getBaseCurrency() {
        return base.getCurrencyUnit();
    }

    public String getQuotedCurrency() {
        return quotes.getCurrencyUnit();
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "base=" + base +
                ", quotes=" + quotes +
                ", rate=" + rate +
                '}';
    }
}
