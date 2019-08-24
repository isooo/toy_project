package me.isooo.bot.application;

import com.linecorp.bot.model.message.*;
import lombok.extern.slf4j.*;
import me.isooo.bot.application.menu.*;
import me.isooo.bot.domain.usermessage.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Slf4j
@Service
public class BotService {
    private final AllCommand allCommand;
    private final UserMessageRepository userMessageRepository;

    public BotService(AllCommand allCommand, UserMessageRepository userMessageRepository) {
        this.allCommand = allCommand;
        this.userMessageRepository = userMessageRepository;
    }

    @Transactional
    public List<Message> handleTextContent(String userId, String userMessage) {
        log.info("userMessage: {}", userMessage);
        final Menu menu = allCommand.getMenu(userMessage);
        final List<Message> messages = menu.getMessages(userId, userMessage);
        userMessageRepository.save(new UserMessage(userId, userMessage));
        return messages;
    }
}
