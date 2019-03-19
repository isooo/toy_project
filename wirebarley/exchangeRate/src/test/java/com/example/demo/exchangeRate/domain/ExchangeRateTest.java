package com.example.demo.exchangeRate.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExchangeRateTest {
    @Test
    public void 생성() {
        // given
        final Country base = Country.USA;
        final Country quoted = Country.KOR;
        double rate = 1000.01;

        // when
        ExchangeRate exchangeRate = new ExchangeRate(base, quoted, rate);

        // then
        assertThat(exchangeRate).isNotNull();
    }

    @Test
    public void 환율_계산() {
        // given
        final double rate = 100.1;
        final double money = 100;
        ExchangeRate exchangeRate = new ExchangeRate(Country.USA, Country.KOR, rate);

        // when
        Money amount = exchangeRate.exchange(new Money(money));

        // then
        assertThat(amount.getValue()).isEqualTo(rate * money);
    }
}