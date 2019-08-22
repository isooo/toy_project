package me.isooo.bot.application;

import com.linecorp.bot.model.message.*;
import me.isooo.bot.domain.usermessage.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@DisplayName("BotService 테스트")
@SpringBootTest
@Transactional
class BotServiceTest {
    @Autowired
    private UserMessageRepository userMessageRepository;

    @Autowired
    private BotService service;

    @DisplayName("help가 입력되었을 때, 가이드 텍스트가 reply되는지 테스트")
    @Test
    void helpGuideText() {
        // given
        final String userId = "test001";
        final String userMessage = "HELP";

        // when
        final List<Message> messages = service.handleTextContent(userId, userMessage);
        final TextMessage message = (TextMessage) messages.get(0);
        final String text = message.getText();

        // then
        assertThat(text).isEqualTo(BotService.HELP_MESSAGE);
    }

    @DisplayName("시작이 입력되었을 때, 기준 통화 목록이 응답되는지 테스트")
    @Test
    void baseCurrencyGuideText() {
        // given
        final String userId = "test001";
        final String userMessage = "시작";

        // when
        final List<Message> messages = service.handleTextContent(userId, userMessage);
        final TextMessage message = (TextMessage) messages.get(0);
        final String text = message.getText();

        // then
        assertThat(text.split("\n")[0]).isEqualTo("기준 통화 선택하기");
    }

    @DisplayName("통화가 최초 입력되었을 때, 기준 통화로 인식하는 지 테스트")
    @Test
    void baseCurrencyHandle() {
        // given
        final String userId = "test001";
        final String userMessage = "KRW";

        // when
        final List<Message> messages = service.handleTextContent(userId, userMessage);
        final TextMessage message = (TextMessage) messages.get(0);
        final String text = message.getText();

        // then
        assertThat(text.split("\n")[0]).isEqualTo("상대 통화 선택하기");
    }

    @DisplayName("통화가 입력되었을 때, 상대 통화로 인식하는 지 테스트")
    @Test
    void counterCurrencyHandle() {
        // given
        final String userId = "test001";
        final String userMessage = "USD";
        this.userMessageRepository.save(new UserMessage(userId, "KRW"));

        // when
        final List<Message> messages = service.handleTextContent(userId, userMessage);
        final TextMessage message = (TextMessage) messages.get(0);
        final String text = message.getText();

        // then
        assertThat(text.split("\n")[0]).isEqualTo("KRW/USD의 환율 : 1000.1929");
    }
}