package me.isooo.bot.support.utils;

import me.isooo.bot.domain.currency.*;

import java.math.*;
import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.regex.*;

public class CurrencyUtils {
    private static final int DEFAULT_CURRENCY_UNIT = 1;
    private static final Pattern CURRENCY_RATE_PATTERN = Pattern.compile("^([A-Z]{6})$");
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("^[0-9]+(?:[.][0-9]+)?$");
    private static final Pattern ALLOWABLE_AMOUNT_PATTERN = Pattern.compile("(^[0-9]{1,8}$)|(^[0-9]{1,6}[.][0-9]{1,2}$)");
    private static final String DECIMAL_FORMAT_PATTERN = "###,##0.00";

    public static boolean isCurrencyRatePattern(String userMessage) {
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
                getFormalizedTimeMessageMessage(currencyRate.getCreatedTime())
        );
    }

    public static String amountResultTextMessageFormatting(String userMessage, CurrencyRate currencyRate) {
        final String formalizedUserMessage = getFormalizedFigures(new BigDecimal(userMessage));
        final String formalizedAmountResult = getFormalizedFigures(currencyRate.calculateAmount(new BigDecimal(userMessage)));
        return String.format("%s%s → %s%s입니다.\n\n%s",
                formalizedUserMessage,
                currencyRate.getBaseUnit(),
                formalizedAmountResult,
                currencyRate.getCounterUnit(),
                getFormalizedTimeMessageMessage(currencyRate.getCreatedTime())
        );
    }

    public static String getFormalizedTimeMessageMessage(LocalDateTime localDateTime) {
        String defaultMessage = "기준 시간 : " + getFormalizedTime(localDateTime);
        if (isOutOfDate(localDateTime)) {
            defaultMessage += "\n(※ 참고 : 24시간 이전 데이터입니다.)";
        }
        return defaultMessage;
    }

    private static String getFormalizedTime(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String getFormalizedFigures(BigDecimal figures) {
        return new DecimalFormat(DECIMAL_FORMAT_PATTERN).format(figures);
    }

    private static boolean isOutOfDate(LocalDateTime localDateTime) {
        return localDateTime.isBefore(LocalDateTime.now().minusDays(1));
    }

    public static boolean isAllowableAmountPattern(final String pattern) {
        return ALLOWABLE_AMOUNT_PATTERN.matcher(pattern).matches();
    }
}
