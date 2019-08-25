package me.isooo.bot.domain.usermessage;

import lombok.*;
import me.isooo.bot.support.utils.*;

@Getter
public enum MessageType {
    TEXT,
    CURRENCY_PAIR;

    public static MessageType getType(final String message) {
        if (CurrencyUtils.isCurrencyRatePattern(message)) {
            return CURRENCY_PAIR;
        }
        return TEXT;
    }
}
