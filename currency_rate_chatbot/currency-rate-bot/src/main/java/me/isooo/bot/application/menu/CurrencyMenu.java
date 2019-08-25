package me.isooo.bot.application.menu;

import com.linecorp.bot.model.action.*;
import com.linecorp.bot.model.message.*;
import com.linecorp.bot.model.message.quickreply.*;
import lombok.extern.slf4j.*;
import me.isooo.bot.domain.currency.Currency;
import me.isooo.bot.domain.currency.*;
import me.isooo.bot.support.utils.*;
import org.springframework.stereotype.*;

import java.util.*;

@Slf4j
@Component
public class CurrencyMenu implements Menu {
    private static final String BASE = "base";
    private static final String COUNTER = "counter";

    @Override
    public List<Message> getMessages(String userId, String userMessage) {
        log.info("userId: {}, userMessage: {}", userId, userMessage);
        try {
            final TextMessage textMessage = getCurrencyRateMessage(userMessage);
            return Collections.singletonList(textMessage);
        } catch (IllegalArgumentException e) {
            log.info("[IllegalArgumentException] invalid currency, userMessage : {}", userMessage);
            return Collections.unmodifiableList(ExceptionMenu.unallowableCurrency(userId, userMessage));
        }
    }

    public boolean matches(String userMessage) {
        return CurrencyUtils.isCurrencyRatePattern(userMessage);
    }

    private TextMessage getCurrencyRateMessage(String userMessage) {
        final CurrencyRate currencyRate = convert(userMessage);
        final TextMessage textMessage = new TextMessage(
                // TODO : #12 업데이트 시간 표기
                CurrencyUtils.currencyRateTextMessageFormatting(currencyRate)
        );

        return textMessage
                .toBuilder()
                .quickReply(
                        QuickReply.items(
                                Arrays.asList(
                                        QuickReplyItem.builder()
                                                .action(new MessageAction("처음으로 돌아가기", StartMenu.Command))
                                                .build(),
                                        QuickReplyItem.builder()
                                                .action(new MessageAction(AmountMenu.Command, AmountMenu.Command))
                                                .build()
                                )
                        )
                ).build();
    }

    private CurrencyRate convert(final String text) {
        final Map<String, Currency> currencyPair = extractCurrencyPair(text);
        final Currency base = currencyPair.get(BASE);
        final Currency counter = currencyPair.get(COUNTER);
        return new CurrencyRate(base, counter);
    }

    private static Map<String, Currency> extractCurrencyPair(final String text) {
        final Map<String, Currency> map = new HashMap<>();
        final Currency base = Currency.valueOf(text.substring(0, 3));
        final Currency counter = Currency.valueOf(text.substring(3));
        map.put(BASE, base);
        map.put(COUNTER, counter);
        return map;
    }
}
