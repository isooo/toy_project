package me.isooo.bot.application.menu;

import com.linecorp.bot.model.message.*;
import lombok.extern.slf4j.*;
import me.isooo.bot.domain.currency.Currency;
import me.isooo.bot.domain.usermessage.*;
import org.springframework.stereotype.*;

import java.util.*;

@Slf4j
@Component
public class AmountMenu implements Menu {
    public static final String COMMAND = "환전 금액 계산하기";
    private static List<String> CANDIDATES;

    private final UserMessageRepository userMessageRepository;

    static {
        CANDIDATES = Arrays.asList(
                COMMAND,
                "환전",
                "환전 금액",
                "환전 계산"
        );
    }

    public AmountMenu(UserMessageRepository userMessageRepository) {
        this.userMessageRepository = userMessageRepository;
    }

    @Override
    public boolean matches(String userMessage) {
        return CANDIDATES.stream()
                .anyMatch(candidate -> candidate.equals(userMessage));
    }

    @Override
    public List<Message> getMessages(String sessionId, String userMessage) {
        log.info("userId: {}, userMessage: {}", sessionId, userMessage);
        try {
            final UserMessage userCurrencyPair = userMessageRepository.findFirstBySessionIdAndMessageTypeOrderByIdDesc(sessionId, MessageType.CURRENCY_PAIR).get();
            log.info("userCurrencyPair: {}", userCurrencyPair);
            final String userCurrencyPairMessage = userCurrencyPair.getMessage();
            final Currency base = Currency.valueOf(userCurrencyPairMessage.substring(0, 3));
            final List<Message> messages = new ArrayList<>();
            messages.add(new TextMessage("환전할 금액(" + base.getUnit() + ")을 숫자로 입력해주세요."));
            messages.add(new TextMessage("※ 숫자는 총 8자리까지 가능하며,\n소수점 이하는 2자리까지 지원합니다."));
            return Collections.unmodifiableList(messages);
        } catch (IllegalArgumentException e) {
            log.error("[IllegalArgumentException]", e);
            return new StartMenu().getMessages(sessionId, userMessage);
        } catch (NoSuchElementException e) {
            log.error("[NoSuchElementException]", e);
            return Collections.unmodifiableList(ExceptionMenu.currencyPairEmpty(sessionId, userMessage));
        }
    }
}
