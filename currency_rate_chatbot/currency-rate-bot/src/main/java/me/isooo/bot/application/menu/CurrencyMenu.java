package me.isooo.bot.application.menu;

import com.linecorp.bot.model.message.*;
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

        final StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isEmpty(latestUserMessage) || !Currency.isCurrency(latestUserMessage.getMessage())) {
            stringBuilder.append("상대 통화 선택하기\n(" + userMessage + " → ???)\n");
            Arrays.stream(me.isooo.bot.domain.currency.Currency.values())
                    .map(c -> c.name())
                    .filter(name -> !name.equals(userMessage))
                    .forEach(name -> stringBuilder.append("\n" + name));
            final TextMessage textMessage = new TextMessage(stringBuilder.toString());
            return Collections.singletonList(textMessage);
        }

        final Currency baseCurrency = Currency.valueOf(latestUserMessage.getMessage());
        final Currency counterCurrency = Currency.valueOf(userMessage);

        stringBuilder.append(baseCurrency.name() + "/" + counterCurrency.name() + "의 환율 : ");
        stringBuilder.append(dummyCurrencyRate + "\n\n");
        stringBuilder.append("※ 기준 시각\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        final TextMessage textMessage = new TextMessage(stringBuilder.toString());
        return Collections.singletonList(textMessage);
    }

    public boolean isCurrencyPattern(final String pattern) {
        return CURRENCY_PATTERN.matcher(pattern).matches();
    }
}
