package me.isooo.bot.support.config;

import lombok.*;
import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.*;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "currency.api")
public class CurrencyApiConfig {
    private String baseUrl;
    private String endpoint;
    private String queryParamNameAccessKey;
    private String accessKey;
    private String rootPath;
}