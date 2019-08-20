package me.isooo.bot;

import com.linecorp.bot.client.*;
import com.linecorp.bot.model.*;
import com.linecorp.bot.model.event.*;
import com.linecorp.bot.model.event.message.*;
import com.linecorp.bot.model.message.*;
import com.linecorp.bot.model.response.*;
import com.linecorp.bot.spring.boot.annotation.*;
import lombok.*;
import lombok.extern.slf4j.*;

import java.util.*;
import java.util.concurrent.*;

@Slf4j
@LineMessageHandler
public class HelloController {
    private LineMessagingClient lineMessagingClient;

    public HelloController(LineMessagingClient lineMessagingClient) {
        this.lineMessagingClient = lineMessagingClient;
    }

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        log.info("event: {}", event);
        final String replyToken = event.getReplyToken();
        reply(replyToken, Collections.singletonList(new TextMessage("hello")));
    }

    @EventMapping
    public void handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
        log.info("event: {}", event);
        final String replyToken = event.getReplyToken();
        reply(replyToken, Collections.singletonList(new TextMessage("hello")));
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        log.info("event(no reply): {}", event);
    }

    private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
        try {
            BotApiResponse apiResponse = lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, messages))
                    .get();
            log.info("Sent messages: {}", apiResponse);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
