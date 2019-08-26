package me.isooo.bot.support.utils;

import me.isooo.bot.domain.currency.Currency;
import me.isooo.bot.domain.currency.*;

import java.math.*;
import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.regex.*;

public class CurrencyUtils {
    private static final int DEFAULT_CURRENCY_UNIT = 1;
    private static final Pattern CURRENCY_RATE_PATTERN = Pattern.compile("^([A-Z]{6})$");
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("^[0-9]+(?:[.][0-9]+)?$");
    private static final Pattern ALLOWABLE_AMOUNT_PATTERN = Pattern.compile("(^[0-9]{1,8}$)|(^[0-9]{1,6}[.][0-9]{1,2}$)");
    private static final String DECIMAL_FORMAT_PATTERN = "###,##0.00";
    private static final String BASE = "base";
    private static final String COUNTER = "counter";

    public static boolean isCurrencyRatePattern(final String userMessage) {
        return CURRENCY_RATE_PATTERN.matcher(userMessage).matches();
    }

    public static boolean isAmountPattern(String userMessage) {
        return AMOUNT_PATTERN.matcher(userMessage).matches();
    }

    public static String currencyRateTextMessageFormatting(CurrencyRate currencyRate) {
        final String formalizedRate = getFormalizedFigures(currencyRate.getRate());
        return String.format("%d%s → %s%s입니다.\n\n※ 기준 시각\n%s",
                DEFAULT_CURRENCY_UNIT,
                currencyRate.getBaseUnit(),
                formalizedRate,
                currencyRate.getCounterUnit(),
                getDefaultFormalizedTimeMessage(currencyRate.getCreatedTime())
        );
    }

    public static String amountResultTextMessageFormatting(String userMessage, CurrencyRate currencyRate) {
        final String formalizedUserMessage = getFormalizedFigures(new BigDecimal(userMessage));
        final String formalizedAmountResult = getFormalizedFigures(currencyRate.calculateAmount(new BigDecimal(userMessage)));
        return String.format("%s%s → %s%s입니다.\n\n※ 기준 시각\n%s",
                formalizedUserMessage,
                currencyRate.getBaseUnit(),
                formalizedAmountResult,
                currencyRate.getCounterUnit(),
                getDefaultFormalizedTimeMessage(currencyRate.getCreatedTime())
        );
    }



    private static String getDefaultFormalizedTimeMessage(final LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static CurrencyRate convert(String text) {
        final Map<String, Currency> currencyPair = extractCurrencyPair(text);
        final Currency base = currencyPair.get(BASE);
        final Currency counter = currencyPair.get(COUNTER);
        return new CurrencyRate(base, counter);
    }

    private static Map<String, Currency> extractCurrencyPair(String text) {
        final Map<String, Currency> map = new HashMap<>();
        final Currency base = Currency.valueOf(text.substring(0, 3));
        final Currency counter = Currency.valueOf(text.substring(3));
        map.put(BASE, base);
        map.put(COUNTER, counter);
        return map;
    }

    public static String getFormalizedFigures(final BigDecimal figures) {
        return new DecimalFormat(DECIMAL_FORMAT_PATTERN).format(figures);
    }
}
