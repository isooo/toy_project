package me.isooo.bot.application;

import com.linecorp.bot.model.message.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;

import java.util.*;

@Slf4j
@Service
public class BotService {
    public final static String HELP_TEXT = "HELP";
    public final static String HELP_MESSAGE = "[시작]을 입력하시면,\n환율 조회를 시작할 수 있어요 :)";

    public List<Message> handleTextContent(String userId, String userMessage) {
        if (HELP_TEXT.equals(userMessage)) {
            final TextMessage textMessage = new TextMessage(HELP_MESSAGE);
            return Collections.singletonList(textMessage);
        }
        return Collections.singletonList(new TextMessage("hello"));
    }
}
