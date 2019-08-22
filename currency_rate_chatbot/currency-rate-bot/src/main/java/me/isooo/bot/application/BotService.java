package me.isooo.bot.application;

import com.linecorp.bot.model.message.*;
import lombok.extern.slf4j.*;
import me.isooo.bot.domain.Currency;
import me.isooo.bot.domain.usermessage.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.*;

import java.math.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

@Slf4j
@Service
public class BotService {
    public final static String HELP_TEXT = "HELP";
    public final static String HELP_MESSAGE = "[시작]을 입력하시면,\n환율 조회를 시작할 수 있어요 :)";
    private final static BigDecimal dummyCurrencyRate = new BigDecimal("1000.1929");

    private final UserMessageRepository userMessageRepository;

    public BotService(UserMessageRepository userMessageRepository) {
        this.userMessageRepository = userMessageRepository;
    }

    @Transactional
    public List<Message> handleTextContent(String userId, String userMessage) {
        log.info("userMessage: {}", userMessage);
        final List<Message> messages = getMessages(userId, userMessage);
        userMessageRepository.save(new UserMessage(userId, userMessage));
        return messages;
    }

    private List<Message> getMessages(String userId, String userMessage) {
        if ("시작".equals(userMessage)) {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("기준 통화 선택하기\n(??? → ???)\n");
            Arrays.stream(Currency.values())
                    .map(c -> c.name())
                    .forEach(name -> stringBuilder.append("\n" + name));
            final TextMessage textMessage = new TextMessage(stringBuilder.toString());
            return Collections.singletonList(textMessage);
        }

        if (Currency.isCurrency(userMessage)) {
            final Optional<UserMessage> latestUserMessage = userMessageRepository.findFirstByUserIdOrderByIdDesc(userId);
            final StringBuilder stringBuilder = new StringBuilder();
            if (StringUtils.isEmpty(latestUserMessage) || !Currency.isCurrency(latestUserMessage.get().getMessage())) {
                stringBuilder.append("상대 통화 선택하기\n(" + userMessage + " → ???)\n");
                Arrays.stream(Currency.values())
                        .map(c -> c.name())
                        .filter(name -> !name.equals(userMessage))
                        .forEach(name -> stringBuilder.append("\n" + name));
                final TextMessage textMessage = new TextMessage(stringBuilder.toString());
                return Collections.singletonList(textMessage);
            }

            final Currency baseCurrency = Currency.valueOf(latestUserMessage.get().getMessage());
            final Currency counterCurrency = Currency.valueOf(userMessage);

            stringBuilder.append(baseCurrency.name() + "/" + counterCurrency.name() + "의 환율 : ");
            stringBuilder.append(dummyCurrencyRate + "\n\n");
            stringBuilder.append("※ 기준 시각\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            final TextMessage textMessage = new TextMessage(stringBuilder.toString());
            return Collections.singletonList(textMessage);
        }
        final TextMessage textMessage = new TextMessage(HELP_MESSAGE);
        return Collections.singletonList(textMessage);
    }
}
