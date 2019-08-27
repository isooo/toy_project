package me.isooo.bot.application;

import me.isooo.bot.domain.currency.*;
import me.isooo.bot.domain.currency.Currency;
import me.isooo.bot.support.config.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class CurrencyRateConverter {
    private static final String BASE = "base";
    private static final String COUNTER = "counter";

    private final CurrencyRateRepository currencyRateRepository;
    private final DefaultBaseCurrencyConfig defaultBaseCurrencyConfig;

    public CurrencyRateConverter(CurrencyRateRepository currencyRateRepository, DefaultBaseCurrencyConfig defaultBaseCurrencyConfig) {
        this.currencyRateRepository = currencyRateRepository;
        this.defaultBaseCurrencyConfig = defaultBaseCurrencyConfig;
    }

    public CurrencyRate convert(String text) {
        final Map<String, Currency> currencyPair = extractCurrencyPair(text);
        final Currency base = currencyPair.get(BASE);
        final Currency counter = currencyPair.get(COUNTER);
        return getLatestCurrencyRate(base, counter);
    }

    private static Map<String, Currency> extractCurrencyPair(String text) {
        final Map<String, Currency> map = new HashMap<>();
        final Currency base = Currency.valueOf(text.substring(0, 3));
        final Currency counter = Currency.valueOf(text.substring(3));
        map.put(BASE, base);
        map.put(COUNTER, counter);
        return map;
    }

    private CurrencyRate getLatestCurrencyRate(Currency base, Currency counter) {
        if (!currencyRateRepository.existsByBaseAndCounter(base, counter)) {
            saveCurrencyRate(base, counter);
        }
        return currencyRateRepository.findFirstByBaseAndCounterOrderByIdDesc(base, counter);
    }

    private void saveCurrencyRate(Currency base, Currency counter) {
        final CurrencyRate baseCurrencyRate = getUsdBasedCurrencyRate(base);
        final CurrencyRate counterCurrencyRate = getUsdBasedCurrencyRate(counter);
        final CurrencyRate calculate = baseCurrencyRate.calculate(counterCurrencyRate);
        currencyRateRepository.save(calculate);
    }

    private CurrencyRate getUsdBasedCurrencyRate(Currency base) {
        final Currency defaultBase = Currency.valueOf(defaultBaseCurrencyConfig.getUnit());
        return currencyRateRepository.findFirstByBaseAndCounterOrderByIdDesc(defaultBase, base);
    }
}
