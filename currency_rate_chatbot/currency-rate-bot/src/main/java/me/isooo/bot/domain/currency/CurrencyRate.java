package me.isooo.bot.domain.currency;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.*;
import java.math.*;
import java.time.*;

@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class CurrencyRate {
    private static final int DIVIDE_SCALE = 6;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Currency base;

    @Enumerated(value = EnumType.STRING)
    private Currency counter;

    @Column(columnDefinition = "DECIMAL(11, 6)")
    private BigDecimal rate;

    @CreationTimestamp
    private LocalDateTime createdTime;

    public CurrencyRate(Currency base, Currency counter, BigDecimal rate) {
        this.base = base;
        this.counter = counter;
        this.rate = rate;
    }

    public CurrencyRate(Currency base, Currency counter, BigDecimal rate, LocalDateTime createdTime) {
        this.base = base;
        this.counter = counter;
        this.rate = rate;
        this.createdTime = createdTime;
    }

    public CurrencyRate calculate(CurrencyRate counter) {
        final BigDecimal rate = counter.rate
                .divide(this.rate, DIVIDE_SCALE, RoundingMode.HALF_EVEN);
        return new CurrencyRate(this.counter, counter.counter, rate, latestCreatedTime(counter));
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

    private LocalDateTime latestCreatedTime(CurrencyRate counter) {
        if (this.createdTime.isBefore(counter.createdTime)) {
            return this.createdTime;
        }
        return counter.createdTime;
    }
}
