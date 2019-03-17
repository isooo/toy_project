package com.example.demo.exchangeRate.infra;

import com.example.demo.exchangeRate.domain.Country;
import com.example.demo.exchangeRate.domain.ExchangeRate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

/**
 * e.g. http://apilayer.net/api/live?access_key=ee50cd7cc73c9b7a7bb3d9617cfb6b9c
 */
public class ExchangeApiClient {
    private static final String BASE_URL = "http://apilayer.net/api/";
    private static final String ENDPOINT = "live";
    private static final String QUERY_PARAM_NAME_ACCESS_KEY = "access_key";
    private static final String ACCESS_KEY = "ee50cd7cc73c9b7a7bb3d9617cfb6b9c";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public ExchangeRate getExchangeRate(
            final Country base,
            final Country quotes
    ) throws IOException {
        final JsonNode root = MAPPER.readTree(getBody(getLiveUri()));
        final double rate = root.path("quotes")
                .path(base.getCurrencyUnit() + quotes.getCurrencyUnit())
                .doubleValue()
                ;
        return new ExchangeRate(base, quotes, rate);
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
