package me.isooo.bot.infra;

import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.*;
import me.isooo.bot.application.*;
import me.isooo.bot.domain.currency.Currency;
import me.isooo.bot.domain.currency.*;
import me.isooo.bot.support.config.*;
import org.springframework.boot.web.client.*;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;
import org.springframework.web.util.*;

import java.math.*;
import java.net.*;
import java.util.*;
import java.util.stream.*;

@Slf4j
@Component
public class CurrencyApiClient implements CurrencyApi {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final CurrencyApiConfig apiConfig;
    private final DefaultBaseCurrencyConfig defaultBaseCurrencyConfig;
    private final RestTemplate restTemplate;

    public CurrencyApiClient(
            CurrencyApiConfig apiConfig,
            DefaultBaseCurrencyConfig defaultBaseCurrencyConfig,
            RestTemplateBuilder restTemplateBuilder
    ) {
        this.apiConfig = apiConfig;
        this.defaultBaseCurrencyConfig = defaultBaseCurrencyConfig;
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public List<CurrencyRate> getRates() {
        log.info("CurrencyApiClient getRates");
        final JsonNode currencyPath = getCurrencyPath();
        final Currency base = Currency.valueOf(defaultBaseCurrencyConfig.getUnit());
        return Arrays.stream(Currency.values())
                .map(counter -> new CurrencyRate(base, counter, this.getRate(currencyPath, counter)))
                .filter(currency -> currency.getRate() != null)
                .collect(Collectors.toList());
    }

    private JsonNode getCurrencyPath() {
        try {
            return MAPPER.readTree(
                    this.getBody(
                            this.makeLiveUri()
                    )
            ).path(apiConfig.getRootPath());
        } catch (Exception e) {
            log.error("getRates : {}", e);
            return null;
        }
    }

    private String getBody(final URI uri) {
        return this.restTemplate.getForEntity(uri, String.class)
                .getBody();
    }

    private URI makeLiveUri() {
        return UriComponentsBuilder.fromHttpUrl(apiConfig.getBaseUrl())
                .path(apiConfig.getEndpoint())
                .queryParam(apiConfig.getQueryParamNameAccessKey(), apiConfig.getAccessKey())
                .build()
                .toUri();
    }

    private BigDecimal getRate(JsonNode currencyPath, Currency counter) {
        return currencyPath.path(defaultBaseCurrencyConfig.getUnit() + counter.name())
                .decimalValue();
    }
}