package com.example.demo.exchangeRate.ui;

import com.example.demo.exchangeRate.application.ExchangeResponse;
import com.example.demo.exchangeRate.application.ExchangeService;
import com.example.demo.exchangeRate.domain.ExchangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ExchangeRestController {
    @Autowired
    private ExchangeService service;

    @GetMapping(value = "/rate")
    public ExchangeRate rate(
            @RequestParam(value = "base") String base,
            @RequestParam(value = "quotes") String quotes
    ) {
        ExchangeRate exchangeRate = null;
        try {
            if(!quotes.isEmpty()) {
                exchangeRate = service.getRateInfo(base, quotes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exchangeRate;
    }

    @GetMapping(value = "/amount")
    public ExchangeResponse amount(
            @RequestParam(value = "base") String base,
            @RequestParam(value = "quotes") String quotes,
            @RequestParam(value = "remittance") double remittance
    ) {
        ExchangeResponse response = null;
        try {
            response = service.getAmount(base, quotes, remittance);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
