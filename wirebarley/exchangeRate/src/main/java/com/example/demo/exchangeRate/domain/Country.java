package com.example.demo.exchangeRate.domain;

public enum Country {
    USA("USD"),
    KOR("KRW"),
    JPN("JPY"),
    PHL("PHP");

    private final String currencyUnit;

    Country(final String currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    public String getCurrencyUnit() {
        return this.currencyUnit;
    }
}