package com.example.demo.exchangeRate.infra;

import com.example.demo.exchangeRate.domain.Country;
import com.example.demo.exchangeRate.domain.ExchangeRate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ExchangeApiClientTest {
    @InjectMocks
    private ExchangeApiClient client;

    @Test
    public void 미국에서_한국_환율() {
        // given

        // when
        final Optional<ExchangeRate> exchangeRate = client.getExchangeRate(Country.USA, Country.KOR);

        // then
//        assertThat(exchangeRate.isPresent()).isTrue();
        assertThat(exchangeRate.get().getRate()).isGreaterThan(0);
    }
}