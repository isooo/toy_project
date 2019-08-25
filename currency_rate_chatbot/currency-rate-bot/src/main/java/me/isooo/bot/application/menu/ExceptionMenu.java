package me.isooo.bot.application.menu;

import com.linecorp.bot.model.message.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;

import java.util.*;

@Slf4j
@Component
public class ExceptionMenu {
    private static final String UNALLOWABLE_CURRENCY_MESSAGE = "가능한 통화 목록을 다시 확인해주세요 :)";
    private static final String CURRENCY_PAIR_EMPTY_MESSAGE = "환율 정보를 먼저 입력해주세요 :)";

    public static List<Message> unallowableCurrency(String userId, String userMessage) {
        log.info("userId: {}, userMessage: {}", userId, userMessage);
        return getMessages(userId, userMessage, UNALLOWABLE_CURRENCY_MESSAGE);
    }

    public static List<Message> currencyPairEmpty(String userId, String userMessage) {
        log.info("userId: {}, userMessage: {}", userId, userMessage);
        return getMessages(userId, userMessage, CURRENCY_PAIR_EMPTY_MESSAGE);
    }

    private static List<Message> getMessages(String userId, String userMessage, String message) {
        final List<Message> messages = new ArrayList<>();
        final TextMessage textMessage = new TextMessage(message);
        messages.add(textMessage);
        messages.addAll(new StartMenu().getMessages(userId, userMessage));
        return Collections.unmodifiableList(messages);
    }
}
