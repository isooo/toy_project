package com.example.demo.exchangeRate.application;

import com.example.demo.exchangeRate.domain.Country;
import com.example.demo.exchangeRate.domain.ExchangeRate;
import com.example.demo.exchangeRate.domain.Money;
import org.springframework.stereotype.Service;

@Service
public class ExchangeService {
    private ExchangeApi exchangeApi;

    public ExchangeService(ExchangeApi exchangeApi) {
        this.exchangeApi = exchangeApi;
    }

    public ExchangeRate getRateInfo(
            final String base,
            final String quotes
    ) {
        return getRateInfo(Country.valueOf(base), Country.valueOf(quotes));
    }

    public ExchangeResponse getAmount(
            final String base,
            final String quotes,
            final double remittance
    ) {
        final Country baseCountry = Country.valueOf(base);
        final Country quotesCountry = Country.valueOf(quotes);
        validate(remittance, baseCountry);
        final ExchangeRate exchangeRate = getRateInfo(baseCountry, quotesCountry);
        final Money exchange = exchangeRate.exchange(new Money(remittance));
        return new ExchangeResponse(exchangeRate, exchange);
    }

    private ExchangeRate getRateInfo(
            final Country baseCountry,
            final Country quotesCountry
    ) {
        return exchangeApi.getExchangeRate(baseCountry, quotesCountry)
                .orElseGet(null)
                ;
    }

    private void validate(final double remittance, final Country baseCountry) {
        if (remittance < 0) {
            throw new IllegalArgumentException("송금액이 바르지 않습니다.");
        }
        if (baseCountry == Country.USA) {
            if (remittance > 10000) {
                throw new IllegalArgumentException("송금액이 바르지 않습니다.");
            }
        }
    }
}