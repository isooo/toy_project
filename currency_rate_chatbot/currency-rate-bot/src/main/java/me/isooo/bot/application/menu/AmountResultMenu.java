package me.isooo.bot.application.menu;

import com.linecorp.bot.model.message.*;
import lombok.extern.slf4j.*;
import me.isooo.bot.domain.currency.*;
import me.isooo.bot.domain.usermessage.*;
import me.isooo.bot.support.utils.*;
import org.springframework.stereotype.*;

import java.math.*;
import java.util.*;

@Slf4j
@Component
public class AmountResultMenu implements Menu {
    private final UserMessageRepository userMessageRepository;

    public AmountResultMenu(UserMessageRepository userMessageRepository) {
        this.userMessageRepository = userMessageRepository;
    }

    @Override
    public List<Message> getMessages(String userId, String userMessage) {
        log.info("userId: {}, userMessage: {}", userId, userMessage);
        // TODO : #13 허용 가능한 포멧인지 체크 (CurrencyUtils.isAllowableAmountPattern())

        try {
            final UserMessage userCurrencyPair = userMessageRepository.findFirstByUserIdAndMessageTypeOrderByIdDesc(userId, MessageType.CURRENCY_PAIR).get();
            log.info("userCurrencyPair: {}", userCurrencyPair);
            final String userCurrencyPairMessage = userCurrencyPair.getMessage();
            final CurrencyRate currencyRate = CurrencyUtils.convert(userCurrencyPairMessage);
            final TextMessage textMessage = getAmountResultMessage(userMessage, currencyRate);
            return Collections.singletonList(textMessage);
        } catch (IllegalArgumentException e) {
            log.error("[IllegalArgumentException]", e);
            return Collections.unmodifiableList(ExceptionMenu.currencyPairEmpty(userId, userMessage));
        }
    }

    private TextMessage getAmountResultMessage(String userMessage, CurrencyRate currencyRate) {
        return new TextMessage(
                CurrencyUtils.amountResultTextMessageFormatting(userMessage, currencyRate)
        );
    }

    public boolean matches(String userMessage) {
        return CurrencyUtils.isAmountPattern(userMessage);
    }
}
