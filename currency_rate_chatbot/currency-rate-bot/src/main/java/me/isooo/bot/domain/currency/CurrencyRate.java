package me.isooo.bot.domain.currency;

import lombok.*;

import java.math.*;
import java.time.*;

@ToString
@Getter
public class CurrencyRate {
    private Currency base;
    private Currency counter;

    // TODO : #20 환율 제공 API 연동 시, 제거 예정
    private final BigDecimal rate = new BigDecimal("1000.1929");

    private LocalDateTime createdTime;

    public CurrencyRate(Currency base, Currency counter) {
        this.base = base;
        this.counter = counter;
        this.createdTime = LocalDateTime.now();
    }

    public BigDecimal calculateAmount(BigDecimal money) {
        return money.multiply(this.rate);
    }
    public String getBaseUnit() {
        return base.getUnit();
    }

    public String getCounterUnit() {
        return counter.getUnit();
    }
}
