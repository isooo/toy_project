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
    public List<Message> getMessages(String userId, String userMessage) {
        log.info("userId: {}, userMessage: {}", userId, userMessage);
        final UserMessage userCurrencyPair = userMessageRepository.findFirstByUserIdAndMessageTypeOrderByIdDesc(userId, MessageType.CURRENCY_PAIR).get();
        log.info("userCurrencyPair: {}", userCurrencyPair);
        try {
            final String userCurrencyPairMessage = userCurrencyPair.getMessage();
            final Currency base = Currency.valueOf(userCurrencyPairMessage.substring(0, 3));
            return Collections.singletonList(new TextMessage("환전할 금액(" + base.getUnit() + ")을 숫자로 입력해주세요."));
        } catch (IllegalArgumentException e) {
            log.info("[IllegalArgumentException] Currency pair does not exist, userMessage : {}, userCurrencyPair : {}", userMessage, userCurrencyPair);
            return Collections.unmodifiableList(ExceptionMenu.currencyPairEmpty(userId, userMessage));
        }
    }

    public boolean matches(String userMessage) {
        return AmountMenu.Command.equals(userMessage);
    }
}
