package com.example.demo.exchangeRate.ui;

import com.example.demo.exchangeRate.application.ExchangeResponse;
import com.example.demo.exchangeRate.application.ExchangeService;
import com.example.demo.exchangeRate.domain.ExchangeRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExchangeRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRestController.class);

    private ExchangeService service;

    public ExchangeRestController(ExchangeService service) {
        this.service = service;
    }

    @GetMapping(value = "/rate")
    public ResponseEntity<ExchangeRate> rate(
            @RequestParam(value = "base") String base,
            @RequestParam(value = "quotes") String quotes
    ) {
        LOGGER.info("/rate {" + "base=" + base + ", quotes=" + quotes + "}");
        return ResponseEntity.ok(service.getRateInfo(base, quotes));
    }

    @GetMapping(value = "/amount")
    public ResponseEntity<ExchangeResponse> amount(
            @RequestParam(value = "base") String base,
            @RequestParam(value = "quotes") String quotes,
            @RequestParam(value = "remittance") double remittance
    ) {
        LOGGER.info("/rate {" + "base=" + base + ", quotes=" + quotes + ", remittance=" + remittance + "}");
        return ResponseEntity.ok(service.getAmount(base, quotes, remittance));
    }
}
