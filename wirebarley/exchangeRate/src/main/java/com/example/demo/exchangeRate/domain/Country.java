package com.example.demo.exchangeRate.domain;

public enum Country {
    USA("USD"),
    KOR("KRW"),
    JPN("JPN"),
    PHL("PHP");

    private final String currencyUnit;

    Country(String currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    public String getCurrencyUnit() {
        return this.currencyUnit;
    }
}