package me.isooo.bot.domain.currency;

import lombok.*;

import java.util.*;

@Getter
public enum Currency {
    KRW("원", "₩"),
    USD("달러", "$"),
    JPY("엔", "¥"),
    EUR("유로", "€"),
    CNY("런민비", "¥"),
    GBP("파운드", "£"),
    AUD("달러", "$"),
    CAD("달러", "C$"),
    HKD("달러", "HK$"),
    THB("바트", "฿");

    private final String unit;
    private final String symbol;

    Currency(String unit, String symbol) {
        this.unit = unit;
        this.symbol = symbol;
    }

    public static boolean isCurrency(String text) {
        return Arrays.stream(Currency.values())
                .anyMatch(c -> c.name().equals(text));
    }
}
