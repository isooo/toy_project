package me.isooo.bot.ui;

import com.linecorp.bot.client.*;
import com.linecorp.bot.model.*;
import com.linecorp.bot.model.event.*;
import com.linecorp.bot.model.event.message.*;
import com.linecorp.bot.model.message.*;
import com.linecorp.bot.model.response.*;
import com.linecorp.bot.spring.boot.annotation.*;
import lombok.*;
import lombok.extern.slf4j.*;
import me.isooo.bot.application.*;

import java.util.*;
import java.util.concurrent.*;

@Slf4j
@LineMessageHandler
public class BotController {
    private final LineMessagingClient lineMessagingClient;
    private final BotService botService;

    public BotController(LineMessagingClient lineMessagingClient, BotService botService) {
        this.lineMessagingClient = lineMessagingClient;
        this.botService = botService;
    }

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        log.info("event: {}", event);
        final String userMessage = event.getMessage().getText().toUpperCase();
        final String userId = event.getSource().getUserId();
        final List<Message> messages = botService.handleTextContent(userId, userMessage);
        reply(event.getReplyToken(), messages);
    }

    @EventMapping
    public void handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
        log.info("event: {}", event);
        final String userId = event.getSource().getUserId();
        final List<Message> messages = botService.handleTextContent(userId, BotService.HELP_TEXT);
        reply(event.getReplyToken(), messages);
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
            log.error("reply: {}", e);
            throw new RuntimeException(e);
        }
    }
}
