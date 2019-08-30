package me.isooo.bot.application.menu;

import com.linecorp.bot.model.message.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;

import java.util.*;

@Slf4j
@Component
public class GuideMenu implements Menu {
    public final static String HELP_TEXT = "HELP";
    public final static String HELP_MESSAGE = "[시작]을 입력하시면,\n환율 조회를 시작할 수 있어요 :)";

    @Override
    public boolean matches(String userMessage) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Message> getMessages(String sessionId, String userMessage) {
        log.info("userId: {}, userMessage: {}", sessionId, userMessage);
        final List<Message> messages = new ArrayList<>();
        final StickerMessage stickerMessage = new StickerMessage("1", "2");
        final TextMessage textMessage = new TextMessage(HELP_MESSAGE);
        messages.add(stickerMessage);
        messages.add(textMessage);
        return Collections.unmodifiableList(messages);
    }
}
