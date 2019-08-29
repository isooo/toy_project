package me.isooo.bot.batch;

import me.isooo.bot.application.*;
import me.isooo.bot.domain.currency.Currency;
import me.isooo.bot.domain.currency.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.math.*;
import java.util.*;
import java.util.stream.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("CurrencyRateBatch 테스트")
@ExtendWith(MockitoExtension.class)
class CurrencyRateBatchTest {
    private CurrencyRateBatch batch;

    @Mock
    private CurrencyApi currencyApi;
    @Mock
    private BatchHistoryRepository batchHistoryRepository;
    @Mock
    private CurrencyRateRepository currencyRateRepository;

    @BeforeEach
    void init() {
        batch = new CurrencyRateBatch(currencyApi,
                batchHistoryRepository,
                currencyRateRepository
        );
    }

    @DisplayName("execute() 성공 테스트")
    @Test
    void executeSuccessTest() {
        // given
        when(currencyApi.getRates())
                .thenReturn(Arrays.stream(Currency.values())
                        .map(c -> new CurrencyRate(Currency.USD, c, BigDecimal.ONE))
                        .collect(Collectors.toList())
                );

        // when
        final BatchStatus status = batch.execute();

        // then
        assertThat(status).isEqualTo(BatchStatus.SUCCESS);
    }

    @DisplayName("execute() 실패 테스트")
    @Test
    void executeFailTest() {
        // given
        when(currencyApi.getRates())
                .thenThrow(new NullPointerException());

        // when
        final BatchStatus status = batch.execute();

        // then
        assertThat(status).isEqualTo(BatchStatus.FAIL);
    }

    @Test
    @DisplayName("execute() 실패 후, execute() 재실행 테스트")
    void executeWorkingWhenBatchResultCheckTest() {
        // given
        when(batchHistoryRepository.findFirstByOrderByIdDesc())
                .thenReturn(new BatchHistory(BatchStatus.FAIL));
        when(currencyApi.getRates())
                .thenReturn(Arrays.stream(Currency.values())
                        .map(c -> new CurrencyRate(Currency.USD, c, BigDecimal.ONE))
                        .collect(Collectors.toList())
                );

        // when
        final BatchStatus batchResultCheck = batch.batchResultCheck();

        // then
        verify(currencyRateRepository).saveAll(any());
        assertThat(batchResultCheck).isEqualTo(BatchStatus.SUCCESS);
    }
}