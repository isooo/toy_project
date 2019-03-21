package com.example.demo.exchangeRate.application;

import com.example.demo.exchangeRate.domain.Country;
import com.example.demo.exchangeRate.domain.ExchangeRate;
import com.example.demo.exchangeRate.domain.InvalidTransferAmount;
import com.example.demo.exchangeRate.domain.Money;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

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
        if (baseCountry.getValue() != "USA") {
            return twoWayQuotation(baseCountry, quotesCountry);
        }
        return exchangeApi.getExchangeRate(baseCountry, quotesCountry)
                .orElseGet(null)
                ;
    }

    private ExchangeRate twoWayQuotation(
            final Country baseCountry,
            final Country quotesCountry
    ) {
        double resultRate;
        double baseRate;
        double quotesRate;
        if (quotesCountry.getValue() == "USA") {
            baseRate = getRate(quotesCountry, baseCountry);
            quotesRate = 1;
        } else {
            baseRate = getRate(Country.USA, baseCountry);
            quotesRate = getRate(Country.USA, quotesCountry);
        }
        resultRate = new BigDecimal(quotesRate)
                .divide(new BigDecimal(baseRate), 8, BigDecimal.ROUND_HALF_UP)
                .doubleValue()
        ;
        return Optional.of(new ExchangeRate(baseCountry, quotesCountry, resultRate)).orElseGet(null);
    }

    private double getRate(Country a, Country b) {
        return exchangeApi
                .getExchangeRate(a, b)
                .get()
                .getRate();
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