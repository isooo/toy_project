package me.isooo.bot.support.utils;

import me.isooo.bot.domain.currency.*;

import java.time.*;
import java.time.format.*;
import java.util.regex.*;

public class CurrencyUtils {
    private static final Pattern CURRENCY_RATE_PATTERN = Pattern.compile("^([A-Z]{6})$");

    public static boolean isCurrencyRatePattern(final String pattern) {
        return CURRENCY_RATE_PATTERN.matcher(pattern).matches();
    }

    public static String currencyRateTextMessageFormatting(CurrencyRate currencyRate) {
        return currencyRate.getBase()
                + "/"
                + currencyRate.getCounter()
                + "의 환율 : "
                + currencyRate.getRate()
                + "\n\n"
                + "※ 기준 시각\n"
                + getDefaultFormalizedTimeMessage(currencyRate.getCreatedTime());
    }

    private static String getDefaultFormalizedTimeMessage(final LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
