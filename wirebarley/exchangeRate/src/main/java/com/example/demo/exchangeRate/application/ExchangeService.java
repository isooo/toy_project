package com.example.demo.exchangeRate.application;

import com.example.demo.exchangeRate.domain.Country;
import com.example.demo.exchangeRate.domain.ExchangeRate;
import com.example.demo.exchangeRate.domain.Money;
import com.example.demo.exchangeRate.infra.ExchangeApiClient;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ExchangeService {

    public ExchangeRate getRateInfo(
            final String baseCountry,
            final String quotesCountry
    ) throws IOException {
        ExchangeApiClient client = new ExchangeApiClient();
        Country base = Country.valueOf(baseCountry);
        Country quotes = Country.valueOf(quotesCountry);
        return client.getExchangeRate(base, quotes);
    }

    public ExchangeResponse getAmount(
            final String baseCountry,
            final String quotesCountry,
            final double remittance
    ) throws IOException {
        final ExchangeRate exchangeRate = getRateInfo(baseCountry, quotesCountry);
        final Money exchange = exchangeRate.exchange(new Money(remittance));
        return new ExchangeResponse(exchangeRate, exchange);
    }
}