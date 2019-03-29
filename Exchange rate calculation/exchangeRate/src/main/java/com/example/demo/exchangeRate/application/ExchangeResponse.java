package com.example.demo.exchangeRate.application;

import com.example.demo.exchangeRate.domain.ExchangeRate;
import com.example.demo.exchangeRate.domain.Money;

public class ExchangeResponse {
    private final ExchangeRate exchangeRate;
    private final Money amount;

    public ExchangeResponse(ExchangeRate exchangeRate, Money amount) {
        this.exchangeRate = exchangeRate;
        this.amount = amount;
    }

    public ExchangeRate getExchangeRate() {
        return exchangeRate;
    }

    public Money getAmount() {
        return amount;
    }
}
