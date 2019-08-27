package me.isooo.bot.application.menu;

import com.linecorp.bot.model.action.*;
import com.linecorp.bot.model.message.*;
import com.linecorp.bot.model.message.quickreply.*;
import lombok.extern.slf4j.*;
import me.isooo.bot.application.*;
import me.isooo.bot.domain.currency.*;
import me.isooo.bot.domain.usermessage.*;
import me.isooo.bot.support.utils.*;
import org.springframework.stereotype.*;

import java.util.*;

@Slf4j
@Component
public class AmountResultMenu implements Menu {
    private final CurrencyRateConverter converter;
    private final UserMessageRepository userMessageRepository;

    public AmountResultMenu(CurrencyRateConverter converter, UserMessageRepository userMessageRepository) {
        this.converter = converter;
        this.userMessageRepository = userMessageRepository;
    }

    @Override
    public boolean matches(String userMessage) {
        return CurrencyUtils.isAmountPattern(userMessage);
    }

    @Override
    public List<Message> getMessages(String userId, String userMessage) {
        log.info("userId: {}, userMessage: {}", userId, userMessage);
        // TODO : #13 허용 가능한 포멧인지 체크 (CurrencyUtils.isAllowableAmountPattern())

        try {
            final UserMessage userCurrencyPair = userMessageRepository.findFirstByUserIdAndMessageTypeOrderByIdDesc(userId, MessageType.CURRENCY_PAIR).get();
            log.info("userCurrencyPair: {}", userCurrencyPair);
            final String userCurrencyPairMessage = userCurrencyPair.getMessage();
            final CurrencyRate currencyRate = converter.convert(userCurrencyPairMessage);
            final TextMessage textMessage = getAmountResultMessage(userMessage, currencyRate);
            return Collections.singletonList(textMessage);
        } catch (IllegalArgumentException e) {
            log.error("[IllegalArgumentException]", e);
            return Collections.unmodifiableList(ExceptionMenu.currencyPairEmpty(userId, userMessage));
        }
    }

    private TextMessage getAmountResultMessage(String userMessage, CurrencyRate currencyRate) {
        final TextMessage textMessage = new TextMessage(
                CurrencyUtils.amountResultTextMessageFormatting(userMessage, currencyRate)
        );

        return textMessage
                .toBuilder()
                .quickReply(
                        QuickReply.items(
                                Collections.singletonList(
                                        QuickReplyItem.builder()
                                                .action(new MessageAction("처음으로 돌아가기", StartMenu.Command))
                                                .build()
                                )
                        )
                ).build();
    }
}
