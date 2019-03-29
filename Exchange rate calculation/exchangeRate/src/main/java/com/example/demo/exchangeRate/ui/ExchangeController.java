package com.example.demo.exchangeRate.ui;

import com.example.demo.exchangeRate.domain.Country;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExchangeController {
    @GetMapping(value = {"/", "/index"})
    public ModelAndView index(
    ) {
        final ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("base", Country.values());
        modelAndView.addObject("quotes", Country.values());
        return modelAndView;
    }
}
