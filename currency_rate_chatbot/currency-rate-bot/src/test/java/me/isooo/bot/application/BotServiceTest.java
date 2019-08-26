package me.isooo.bot.application;

import com.linecorp.bot.model.message.*;
import me.isooo.bot.application.menu.*;
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
    private final String userId = "test001";
    @Autowired
    private UserMessageRepository userMessageRepository;

    @Autowired
    private BotService service;

    @DisplayName("help가 입력되었을 때, 가이드 텍스트가 reply되는지 테스트")
    @Test
    void helpGuideText() {
        // given
        final String userMessage = "help";

        // when
        final List<Message> messages = service.handleTextContent(userId, userMessage);
        final TextMessage message = (TextMessage) messages.get(1);
        final String text = message.getText();

        // then
        assertThat(text).isEqualTo(GuideMenu.HELP_MESSAGE);
    }

    @DisplayName("시작이 입력되었을 때, 환율 입력에 대한 가이드가 응답되는지 테스트")
    @Test
    void currencyGuideText() {
        // given
        final String userMessage = "시작";

        // when
        final List<Message> messages = service.handleTextContent(userId, userMessage);
        final TextMessage message = (TextMessage) messages.get(0);
        final String text = message.getText();

        // then
        assertThat(text).isEqualTo("원하시는 환율에 대한 통화를 차례로 입력해주세요.\n" +
                "e.g. 1유로(EUR) 대비 원화(KRW)가 궁금할 땐\n[EURKRW]를 입력하시면 됩니다 :)");
    }

    @DisplayName("올바른 통화 쌍이 입력되었을 때, 환율이 제공되는지 테스트")
    @Test
    void currencyRateInfoText() {
        // given
        final String userMessage = "KRWUSD";

        // when
        final List<Message> messages = service.handleTextContent(userId, userMessage);
        final TextMessage message = (TextMessage) messages.get(0);
        final String text = message.getText();

        // then
        assertThat(text.split("\n")[0]).isEqualTo("KRW/USD의 환율 : 1000.1929");
    }

    @DisplayName("제공되지 않는 통화 쌍이 입력되었을 때, 예외 메시지 테스트")
    @Test
    void unallowableCurrencyRateGuideText() {
        // given
        final String userMessage = "XXXYYY";

        // when
        final List<Message> messages = service.handleTextContent(userId, userMessage);
        final TextMessage message = (TextMessage) messages.get(0);
        final String text = message.getText();

        // then
        assertThat(text).isEqualTo("가능한 통화 목록을 다시 확인해주세요 :)");
    }

    @DisplayName("환전 금액 계산하기 입력 시, 금액 입력 가이드 제공 테스트")
    @Test
    void amountMenuGuideText() {
        // given
        final String userMessage = "환전 금액 계산하기";
        this.userMessageRepository.save(new UserMessage(userId, "KRWUSD"));

        // when
        final List<Message> messages = service.handleTextContent(userId, userMessage);
        final TextMessage message = (TextMessage) messages.get(0);
        final String text = message.getText();

        // then
        assertThat(text).isEqualTo("환전할 금액(원)을 숫자로 입력해주세요.");
    }

    @DisplayName("환율 조회 없이 환전액 계산하기 입력 시, 예외 메시지 테스트")
    @Test
    void currencyRateEmptyGuideText() {
        // given
        final String userMessage = "환전 금액 계산하기";

        // when
        final List<Message> messages = service.handleTextContent(userId, userMessage);
        final TextMessage message = (TextMessage) messages.get(0);
        final String text = message.getText();

        // then
        assertThat(text).isEqualTo("환율 정보를 먼저 입력해주세요 :)");
    }

    @DisplayName("금액 입력 시, 환전액 제공 테스트")
    @Test
    void amountResultText() {
        // given
        final String userMessage = "1000";
        this.userMessageRepository.save(new UserMessage(userId, "KRWUSD"));

        // when
        final List<Message> messages = service.handleTextContent(userId, userMessage);
        final TextMessage message = (TextMessage) messages.get(0);
        final String text = message.getText();

        // then
        assertThat(text).isEqualTo("1000원 → 1,000,192.90달러입니다.");
    }

    @DisplayName("올바른 환율 조회 없이 금액 입력 시, 예외 메시지 테스트")
    @Test
    void currencyRateEmptyGuideText2() {
        // given
        final String userMessage = "1000";
        this.userMessageRepository.save(new UserMessage(userId, "XXXYYY"));

        // when
        final List<Message> messages = service.handleTextContent(userId, userMessage);
        final TextMessage message = (TextMessage) messages.get(0);
        final String text = message.getText();

        // then
        assertThat(text).isEqualTo("환율 정보를 먼저 입력해주세요 :)");
    }

}
