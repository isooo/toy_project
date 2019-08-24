package me.isooo.bot.application.menu;

import com.linecorp.bot.model.action.*;
import com.linecorp.bot.model.message.*;
import com.linecorp.bot.model.message.quickreply.*;
import lombok.extern.slf4j.*;
import me.isooo.bot.domain.currency.Currency;
import me.isooo.bot.domain.usermessage.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;

import java.math.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.regex.*;

@Slf4j
@Component
public class CurrencyMenu implements Menu {
    private static final Pattern CURRENCY_PATTERN = Pattern.compile("^([A-Z]{3})$");

    // TODO : #20 환율 제공 API 연동 시, 제거 예정
    private final static BigDecimal dummyCurrencyRate = new BigDecimal("1000.1929");

    private final UserMessageRepository userMessageRepository;

    public CurrencyMenu(UserMessageRepository userMessageRepository) {
        this.userMessageRepository = userMessageRepository;
    }

    @Override
    public List<Message> getMessages(String userId, String userMessage) {
        if (!Currency.isCurrency(userMessage)) {
            return new ExceptionMenu().getMessages();
        }

        final UserMessage latestUserMessage = userMessageRepository.findFirstByUserIdOrderByIdDesc(userId).orElse(null);
        log.info("latestUserMessage: {}", latestUserMessage);

        if (isBaseCurrency(latestUserMessage)) {
            final TextMessage textMessage = getCounterCurrencyMessage(userMessage);
            return Collections.singletonList(textMessage);
        }

        final TextMessage textMessage = getCurrencyRateMessage(userMessage, latestUserMessage);
        return Collections.singletonList(textMessage);
    }

    private TextMessage getCurrencyRateMessage(String userMessage, UserMessage latestUserMessage) {
        final StringBuilder stringBuilder = new StringBuilder();
        final Currency baseCurrency = Currency.valueOf(latestUserMessage.getMessage());
        final Currency counterCurrency = Currency.valueOf(userMessage);

        stringBuilder.append(baseCurrency.name() + "/" + counterCurrency.name() + "의 환율 : ");
        stringBuilder.append(dummyCurrencyRate + "\n\n");
        stringBuilder.append("※ 기준 시각\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return new TextMessage(stringBuilder.toString())
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

    private TextMessage getCounterCurrencyMessage(String userMessage) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("상대 통화 선택하기\n(" + userMessage + " → ???)\n");
        Arrays.stream(Currency.values())
                .map(c -> c.name())
                .filter(name -> !name.equals(userMessage))
                .forEach(name -> stringBuilder.append("\n" + name));
        return new TextMessage(stringBuilder.toString());
    }

    private boolean isBaseCurrency(UserMessage latestUserMessage) {
        return StringUtils.isEmpty(latestUserMessage) || !Currency.isCurrency(latestUserMessage.getMessage());
    }

    public boolean isCurrencyPattern(final String pattern) {
        return CURRENCY_PATTERN.matcher(pattern).matches();
    }
}
