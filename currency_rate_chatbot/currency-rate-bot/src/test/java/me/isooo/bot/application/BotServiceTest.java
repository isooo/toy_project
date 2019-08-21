package me.isooo.bot.application;

import com.linecorp.bot.model.message.*;
import lombok.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@DisplayName("BotService 테스트")
@SpringBootTest
class BotServiceTest {
    @Autowired
    private BotService service;

    @DisplayName("help가 입력되었을 때, 가이드 텍스트가 reply되는지 테스트")
    @Test
    void name() {
        // given
        final String userMessage = "HELP";

        // when
        final List<Message> messages = service.handleTextContent(userMessage);
        final TextMessage message = (TextMessage) messages.get(0);
        final String text = message.getText();

        // then
        assertThat(text).isEqualTo(BotService.HELP_MESSAGE);
    }
}