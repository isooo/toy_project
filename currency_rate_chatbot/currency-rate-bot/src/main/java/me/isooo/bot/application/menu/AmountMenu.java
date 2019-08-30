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
    public static final String Command = "환전 금액 계산하기";

    private final UserMessageRepository userMessageRepository;

    public AmountMenu(UserMessageRepository userMessageRepository) {
        this.userMessageRepository = userMessageRepository;
    }

    @Override
    public boolean matches(String userMessage) {
        return AmountMenu.Command.equals(userMessage);
    }

    @Override
    public List<Message> getMessages(String sessionId, String userMessage) {
        log.info("userId: {}, userMessage: {}", sessionId, userMessage);
        try {
            final UserMessage userCurrencyPair = userMessageRepository.findFirstBySessionIdAndMessageTypeOrderByIdDesc(sessionId, MessageType.CURRENCY_PAIR).get();
            log.info("userCurrencyPair: {}", userCurrencyPair);
            final String userCurrencyPairMessage = userCurrencyPair.getMessage();
            final Currency base = Currency.valueOf(userCurrencyPairMessage.substring(0, 3));
            return Collections.singletonList(new TextMessage("환전할 금액(" + base.getUnit() + ")을 숫자로 입력해주세요."));
        } catch (IllegalArgumentException e) {
            log.error("[IllegalArgumentException]", e);
            return new StartMenu().getMessages(sessionId, userMessage);
        } catch (NoSuchElementException e) {
            log.error("[NoSuchElementException]", e);
            return Collections.unmodifiableList(ExceptionMenu.currencyPairEmpty(sessionId, userMessage));
        }
    }
}
