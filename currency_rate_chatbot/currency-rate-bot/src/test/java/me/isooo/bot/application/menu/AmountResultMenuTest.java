package me.isooo.bot.application.menu;

import com.linecorp.bot.model.message.*;
import me.isooo.bot.application.*;
import me.isooo.bot.domain.currency.Currency;
import me.isooo.bot.domain.currency.*;
import me.isooo.bot.domain.usermessage.*;
import me.isooo.bot.support.utils.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.math.*;
import java.text.*;
import java.time.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("AmountResultMenu 테스트")
@ExtendWith(MockitoExtension.class)
class AmountResultMenuTest {
    @Mock
    private CurrencyRateConverter converter;

    @Mock
    private UserMessageRepository userMessageRepository;

    @InjectMocks
    private AmountResultMenu amountResultMenu;

    private final String userId = "test001";

    @DisplayName("유저가 통화를 입력하지 않은 상태에서 올바른 포맷의 금액을 입력했을 때, 통화 입력에 대한 가이드 테스트")
    @Test
    void currencyPairEmptyAndAllowableAmountPatternThenExceptionMenuTest() {
        // given
        final String userMessage = "1000";
        when(userMessageRepository.findFirstByUserIdAndMessageTypeOrderByIdDesc(any(), any()))
                .thenReturn(Optional.empty());

        // when
        final List<Message> messages = amountResultMenu.getMessages(userId, userMessage);
        final TextMessage message = (TextMessage) messages.get(0);

        // then
        assertThat(message.getText()).isEqualTo("환율 정보를 먼저 입력해주세요 :)");
    }

    @DisplayName("유저가 유효하지 않은 통화를 입력하고 올바른 포맷의 금액을 입력했을 때, 통화 입력에 대한 가이드 테스트")
    @Test
    void unallowableCurrencyAndAllowableAmountPatternThenExceptionMenuTest() {
        // given
        final String userMessage = "1000";
        final String userCurrencyPair = "XXXYYY";
        when(userMessageRepository.findFirstByUserIdAndMessageTypeOrderByIdDesc(any(), any()))
                .thenReturn(Optional.of(new UserMessage(userId, userCurrencyPair)));
        when(converter.convert(userCurrencyPair))
                .thenThrow(new IllegalArgumentException());

        // when
        final List<Message> messages = amountResultMenu.getMessages(userId, userMessage);
        final TextMessage message = (TextMessage) messages.get(0);

        // then
        assertThat(message.getText()).isEqualTo("환율 정보를 먼저 입력해주세요 :)");
    }

    @DisplayName("유효한 통화 정보가 존재하고 올바른 포맷의 금액을 입력했을 때, 환산액 가이드 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"12345678", "123456.12", "1", "100"})
    void allowableCurrencyAndAllowableAmountPatternThenSuccess(String userMessage) {
        // given
        final String userCurrencyPair = "USDEUR";
        final LocalDateTime now = LocalDateTime.now();
        final BigDecimal rate = new BigDecimal("1.123456");
        when(userMessageRepository.findFirstByUserIdAndMessageTypeOrderByIdDesc(any(), any()))
                .thenReturn(Optional.of(new UserMessage(userId, userCurrencyPair)));
        when(converter.convert(any()))
                .thenReturn(new CurrencyRate(Currency.USD, Currency.EUR, rate, now));

        // when
        final List<Message> messages = amountResultMenu.getMessages(userId, userMessage);
        final TextMessage message = (TextMessage) messages.get(0);

        // then
        assertThat(message.getText())
                .isEqualTo(
                        new DecimalFormat("###,##0.00").format(new BigDecimal(userMessage)) +
                                "달러 → " +
                                new DecimalFormat("###,##0.00").format(new BigDecimal(userMessage).multiply(rate)) +
                                "유로입니다.\n\n" +
                                CurrencyUtils.getFormalizedTimeMessageMessage(now)
                );
    }

    @DisplayName("유효한 통화 정보가 존재하고 올바르지 않은 포맷의 금액을 입력했을 때, 금액 입력 가이드 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"123456789", "123456.123"})
    void allowableCurrencyAndUnallowableAmountPatternThenExceptionMenuTest(String userMessage) {
        // given
        final String userCurrencyPair = "USDEUR";
        final LocalDateTime now = LocalDateTime.now();
        when(userMessageRepository.findFirstByUserIdAndMessageTypeOrderByIdDesc(any(), any()))
                .thenReturn(Optional.of(new UserMessage(userId, userCurrencyPair)));

        // when
        final List<Message> messages = amountResultMenu.getMessages(userId, userMessage);
        final TextMessage message = (TextMessage) messages.get(0);

        // then
        assertThat(message.getText().split("\n")[0])
                .isEqualTo("※ 입력 가능한 금액 형식");
    }
}