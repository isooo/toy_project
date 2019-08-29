package me.isooo.bot.domain.currency;

import org.junit.jupiter.api.*;

import java.math.*;
import java.time.*;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CurrencyRate 테스트")
class CurrencyRateTest {

    @DisplayName("환율 계산 테스트")
    @Test
    void calculateTest() {
        // givne
        final CurrencyRate currencyRateA = new CurrencyRate(Currency.USD, Currency.KRW, new BigDecimal("2.0000"), LocalDateTime.now());
        final CurrencyRate currencyRateB = new CurrencyRate(Currency.USD, Currency.AUD, new BigDecimal("6.0000"), LocalDateTime.now());

        // when
        final CurrencyRate currencyRate = currencyRateA.calculate(currencyRateB);

        // then
        assertThat(currencyRate.getRate()).isEqualTo(new BigDecimal("3.000000"));
    }

    @DisplayName("환전 테스트")
    @Test
    void calculateAmountTest() {
        // givne
        final CurrencyRate currencyRate = new CurrencyRate(Currency.USD, Currency.KRW, new BigDecimal("2.9876"), LocalDateTime.now());

        // when
        final BigDecimal amount = currencyRate.calculateAmount(new BigDecimal("10"));

        // then
        assertThat(amount).isEqualTo(new BigDecimal("29.8760"));
    }

    @DisplayName("최신 환율 테스트")
    @Test
    void latestCreatedTimeTest() {
        // givne
        final CurrencyRate currencyRateA = new CurrencyRate(Currency.USD, Currency.KRW, new BigDecimal("2.0000"), LocalDateTime.now());
        final CurrencyRate currencyRateB = new CurrencyRate(Currency.USD, Currency.AUD, new BigDecimal("6.0000"), LocalDateTime.now().minusYears(1));

        // when
        final CurrencyRate currencyRate = currencyRateA.calculate(currencyRateB);

        // then
        assertThat(currencyRate.getCreatedTime()).isEqualTo(currencyRateB.getCreatedTime());
    }
}