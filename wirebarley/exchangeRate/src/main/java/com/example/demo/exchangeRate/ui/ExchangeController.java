package com.example.demo.exchangeRate.ui;

import com.example.demo.exchangeRate.domain.Country;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExchangeController {
    @GetMapping("/")
    public ModelAndView index(
            @RequestParam(value = "base", defaultValue = "USA") String base
    ) {
        final Country baseCountry = Country.valueOf(base);
        final ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("base", baseCountry);
        modelAndView.addObject("quotes", Country.valuesExcludeBase(baseCountry));
        return modelAndView;
    }
}
