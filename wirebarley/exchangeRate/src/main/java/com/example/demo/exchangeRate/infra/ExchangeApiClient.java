package com.example.demo.exchangeRate.infra;

import com.example.demo.exchangeRate.application.ExchangeApi;
import com.example.demo.exchangeRate.domain.Country;
import com.example.demo.exchangeRate.domain.ExchangeRate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

/**
 * e.g. http://apilayer.net/api/live?access_key=ee50cd7cc73c9b7a7bb3d9617cfb6b9c
 */
@Component
public class ExchangeApiClient implements ExchangeApi {
    private static final String BASE_URL = "http://apilayer.net/api/";
    private static final String ENDPOINT = "live";
    private static final String QUERY_PARAM_NAME_ACCESS_KEY = "access_key";
    private static final String ACCESS_KEY = "eb383804a4a87fe1dda15d097594a941";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public Optional<ExchangeRate> getExchangeRate(
            final Country base,
            final Country quotes
    ) {
        try {
            final JsonNode root = MAPPER.readTree(getBody(getLiveUri()));
            final double rate = root.path("quotes")
                    .path(base.getCurrencyUnit() + quotes.getCurrencyUnit())
                    .doubleValue()
                    ;
            return Optional.of(new ExchangeRate(base, quotes, rate));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private URI getLiveUri() {
        return UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path(ENDPOINT)
                .queryParam(QUERY_PARAM_NAME_ACCESS_KEY, ACCESS_KEY)
                .build()
                .toUri()
                ;
    }

    private String getBody(final URI uri) {
        return new RestTemplate().getForEntity(uri, String.class)
                .getBody()
                ;
    }
}
