package com.example.demo.exchangeRate.application;

import com.example.demo.exchangeRate.domain.Country;
import com.example.demo.exchangeRate.domain.ExchangeRate;
import com.example.demo.exchangeRate.domain.Money;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ExchangeServiceTest {
    @Test
    public void 환율_정보_구하기() throws IOException {
        // given
        ExchangeService service = new ExchangeService();

        // when
        ExchangeRate exchangeRate = service.getRateInfo("USA", "PHL");

        // then
        assertThat(exchangeRate.getRate()).isInstanceOf(Double.class);
        assertThat(exchangeRate.getRate()).isGreaterThan(0);
    }

    @Test
    public void 수취_금액_구하기() throws IOException {
        // given
        ExchangeService service = new ExchangeService();

        // when
        ExchangeResponse response = service.getAmount("USA", "JPN", 1000);
        Money amount = response.getAmount();

        // then
        assertThat(amount.getValue()).isGreaterThan(0);
    }
}