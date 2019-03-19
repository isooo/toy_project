package com.example.demo.exchangeRate.domain;

import org.junit.Test;

public class MoneyTest {
    @Test(expected = InvalidTransferAmount.class)
    public void 잘못된_금액_입력() {
        // given

        // when
        new Money(-1);

        // then
    }
}