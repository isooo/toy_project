package me.isooo.bot.domain.currency;

import org.springframework.data.jpa.repository.*;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Currency> {
    CurrencyRate findFirstByBaseAndCounterOrderByIdDesc(Currency base, Currency counter);

    boolean existsByBaseAndCounter(Currency base, Currency counter);
}