package me.isooo.bot.support.config;

import lombok.*;
import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.*;

@Getter
@Setter
@Configuration
@ConfigurationProperties("base.currency")
public class DefaultBaseCurrencyConfig {
    private String unit;
}
