package com.example.demo.exchangeRate.application;

import com.example.demo.exchangeRate.domain.ExchangeRate;
import com.example.demo.exchangeRate.domain.InvalidTransferAmount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ExchangeServiceTest {
    @Mock
    private ExchangeApi exchangeApi;

    @InjectMocks
    private ExchangeService exchangeService;

    @Test
    public void 환율_정보_구하기() {
        // given
        given(exchangeApi.getExchangeRate(any(), any()))
                .willReturn(Optional.of(new ExchangeRate(any(), any(), 100)))
                ;

        // when
        final ExchangeRate exchangeRate = exchangeService.getRateInfo("USA", "PHL");

        // then
        assertThat(exchangeRate.getRate()).isInstanceOf(Double.class);
        assertThat(exchangeRate.getRate()).isGreaterThan(0);
    }

    @Test
    public void 송금국이_미국이_아닐떄_환율_정보_구하기() {
        // given
        given(exchangeApi.getExchangeRate(any(), any()))
                .willReturn(Optional.of(new ExchangeRate(any(), any(), 100)))
                ;

        // when
        final ExchangeRate exchangeRate = exchangeService.getRateInfo("KOR", "JPN");

        // then
        assertThat(exchangeRate.getRate()).isInstanceOf(Double.class);
        assertThat(exchangeRate.getRate()).isGreaterThan(0);
    }

    @Test
    public void 수취_금액_구하기() {
        // given
        final double rate = 100;
        final double remittance = 1000;
        given(exchangeApi.getExchangeRate(any(), any()))
                .willReturn(Optional.of(new ExchangeRate(any(), any(), rate)))
        ;

        // when
        final ExchangeResponse response = exchangeService.getAmount("USA", "JPN", remittance);

        // then
        assertThat(response.getAmount().getValue()).isEqualTo(remittance * rate);
    }

    @Test(expected = InvalidTransferAmount.class)
    public void 수취금액_입력_오류_영보다_작은_금액() {
        // given
        given(exchangeApi.getExchangeRate(any(), any()))
                .willReturn(Optional.of(new ExchangeRate(any(), any(), 100)))
        ;

        // when
        exchangeService.getAmount("USA", "JPN", -1);

        // then
    }

    @Test(expected = InvalidTransferAmount.class)
    public void 수취금액_입력_오류_만_달러보다_큰_금액() {
        // given

        // when
        exchangeService.getAmount("USA", "JPN", 10_001);

        // then
    }

    @Test(expected = IllegalArgumentException.class)
    public void 잘못된_국가명() {
        // given

        // when
        exchangeService.getAmount("KKK", "KKK", 100);

        // then
    }
}