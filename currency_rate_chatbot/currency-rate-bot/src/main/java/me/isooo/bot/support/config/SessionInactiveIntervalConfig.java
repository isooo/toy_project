package me.isooo.bot.support.config;

import lombok.*;
import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.*;

@Getter
@Setter
@Configuration
@ConfigurationProperties("session")
public class SessionInactiveIntervalConfig {
    private String maxInactiveInterval;
}
