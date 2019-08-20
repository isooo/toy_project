package me.isooo.bot;

import com.linecorp.bot.model.event.*;
import com.linecorp.bot.model.event.message.*;
import com.linecorp.bot.spring.boot.annotation.*;
import lombok.extern.slf4j.*;

@Slf4j
@LineMessageHandler
public class HelloController {
    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        log.info("event: {}", event);
        final String originalMessageText = event.getMessage().getText();
        log.info("originalMessageText: {}", originalMessageText);
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        log.info("event: {}", event);
    }
}
