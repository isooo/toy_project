package com.example.demo.exchangeRate.application;

import com.example.demo.exchangeRate.domain.Country;
import com.example.demo.exchangeRate.domain.ExchangeRate;
import com.example.demo.exchangeRate.domain.InvalidTransferAmount;
import com.example.demo.exchangeRate.domain.Money;
import org.springframework.stereotype.Service;

@Service
public class ExchangeService {
    private static final int DOLLAR_UPPER_LIMIT = 10_000;

    private ExchangeApi exchangeApi;

    public ExchangeService(final ExchangeApi exchangeApi) {
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
        if (baseCountry == Country.USA) {
            checkDollarUpperLimit(remittance);
        }
    }

    private void checkDollarUpperLimit(double remittance) {
        if (remittance > DOLLAR_UPPER_LIMIT) {
            throw new InvalidTransferAmount();
        }
    }
}