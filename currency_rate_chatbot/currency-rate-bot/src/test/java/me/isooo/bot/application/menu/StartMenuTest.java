package me.isooo.bot.application.menu;

import com.linecorp.bot.model.message.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@DisplayName("StartMenu 테스트")
class StartMenuTest {
    private final String sessionId = "test001";

    @DisplayName("유사 명령어 인식 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"환율", "시작", "환율 조회", "환율 정보"})
    void similarCommandsTest(String userMessage) {
        // give
        // when
        final List<Message> messages = new StartMenu().getMessages(sessionId, userMessage);
        final TextMessage message = (TextMessage) messages.get(0);
        final String text = message.getText();

        // then
        assertThat(text).isEqualTo("원하시는 환율에 대한 통화를 차례로 입력해주세요.\n" +
                "e.g. 1유로(EUR) 대비 원화(KRW)가 궁금할 땐\n[EURKRW]를 입력하시면 됩니다 :)");
    }
}