package com.example.demo.exchangeRate.infra;

import com.example.demo.exchangeRate.domain.Country;
import com.example.demo.exchangeRate.domain.ExchangeRate;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ExchangeApiClientTest {
    @Test
    public void 미국에서_한국_환율() throws IOException {
        // given
        final ExchangeApiClient client = new ExchangeApiClient();

        // when
        final ExchangeRate exchangeRate = client.getExchangeRate(Country.USA, Country.KOR);

        // then
        assertThat(exchangeRate.getRate()).isGreaterThan(0);
    }
}