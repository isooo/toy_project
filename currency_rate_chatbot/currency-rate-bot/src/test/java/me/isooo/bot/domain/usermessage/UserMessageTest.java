package me.isooo.bot.domain.usermessage;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserMessage 테스트")
class UserMessageTest {
    private final String userId = "test001";

    @DisplayName("userMessage가 텍스트일 떄, MessageType 타입 테스트")
    @Test
    void messageTypeTextTest() {
        //given
        // when
        final UserMessage userMessage = new UserMessage(userId, "시작");

        // then
        assertThat(userMessage.getMessageType()).isEqualTo(MessageType.TEXT);
    }

    @DisplayName("userMessage가 USDKRW일 떄, MessageType 타입 테스트")
    @Test
    void messageTypeCurrencyPairTest() {
        //given
        // when
        final UserMessage userMessage = new UserMessage(userId, "USDKRW");

        // then
        assertThat(userMessage.getMessageType()).isEqualTo(MessageType.CURRENCY_PAIR);
    }
}