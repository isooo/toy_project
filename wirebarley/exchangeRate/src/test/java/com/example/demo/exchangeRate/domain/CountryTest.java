package com.example.demo.exchangeRate.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CountryTest {
    @Test
    public void 통화정보() {
        // given

        // when
        Country country = Country.USA;

        // then
        assertThat(country.getCurrencyUnit()).isEqualTo("USD");
    }
}