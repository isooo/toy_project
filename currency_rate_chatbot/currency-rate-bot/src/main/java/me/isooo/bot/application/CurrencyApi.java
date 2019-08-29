package me.isooo.bot.application;

import me.isooo.bot.domain.currency.*;

import java.util.*;

public interface CurrencyApi {
    List<CurrencyRate> getRates();
}
