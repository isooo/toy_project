package me.isooo.bot.application.menu;

import com.linecorp.bot.model.message.*;
import me.isooo.bot.domain.usermessage.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("AmountMenu 테스트")
@ExtendWith(MockitoExtension.class)
class AmountMenuTest {
    @Mock
    private UserMessageRepository userMessageRepository;

    @InjectMocks
    private AmountMenu amountMenu;

    private final String sessionId = "test001";

    @DisplayName("유사 명령어 인식 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"환전", "환전 계산"})
    void similarCommandsTest(String userMessage) {
        // given
        when(userMessageRepository.findFirstBySessionIdAndMessageTypeOrderByIdDesc(any(), any()))
                .thenReturn(Optional.of(new UserMessage(sessionId, "KRWUSD")));

        // when
        final List<Message> messages = amountMenu.getMessages(sessionId, userMessage);
        final TextMessage message = (TextMessage) messages.get(0);

        // then
        assertThat(message.getText()).isEqualTo("환전할 금액(원)을 숫자로 입력해주세요.");
    }
}