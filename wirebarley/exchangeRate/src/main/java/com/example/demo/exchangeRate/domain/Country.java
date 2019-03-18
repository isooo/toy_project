package com.example.demo.exchangeRate.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Country {
    USA("미국", "USD"),
    KOR("한국", "KRW"),
    JPN("일본", "JPY"),
    PHL("필리핀", "PHP");

    private final String name;
    private final String currencyUnit;

    Country(final String name, final String currencyUnit) {
        this.name = name;
        this.currencyUnit = currencyUnit;
    }

    public String getValue() {
        return name();
    }

    public String getName() {
        return this.name;
    }

    public String getCurrencyUnit() {
        return this.currencyUnit;
    }

    public static List<Country> valuesExcludeBase(final Country base) {
        return Arrays.stream(Country.values())
                .filter(country -> country != base)
                .collect(Collectors.toList())
                ;
    }
}