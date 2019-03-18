package com.example.demo.exchangeRate.application;

import com.example.demo.exchangeRate.domain.Country;
import com.example.demo.exchangeRate.domain.ExchangeRate;

import java.util.Optional;

public interface ExchangeApi {
    Optional<ExchangeRate> getExchangeRate(Country base, Country quotes);
}
