package com.example.demo.exchangeRate.infra;

import com.example.demo.exchangeRate.application.ExchangeApi;
import com.example.demo.exchangeRate.domain.Country;
import com.example.demo.exchangeRate.domain.ExchangeRate;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ExchangeApiClientTest {
    @Test
    public void 미국에서_한국_환율() {
        // given
        final ExchangeApi exchangeApi = new ExchangeApiClient();

        // when
        final Optional<ExchangeRate> exchangeRate = exchangeApi.getExchangeRate(Country.USA, Country.KOR);

        // then
//        assertThat(exchangeRate.isPresent()).isTrue();
        assertThat(exchangeRate.get().getRate()).isGreaterThan(0);
    }
}