package com.example.demo.exchangeRate.ui;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ExchangeRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void 환율_정보_요청() throws Exception {
        this.mockMvc
                .perform(
                        get("/rate")
                                .param("base", "USA")
                                .param("quotes", "JPN")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("quotedCurrency").value("JPY")
                ).andExpect(jsonPath("rate").isNumber()
                );
    }

    @Test
    public void 수취_금액_요청() throws Exception {
        this.mockMvc
                .perform(
                        get("/amount")
                                .param("base", "USA")
                                .param("quotes", "PHL")
                                .param("remittance", "111")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("exchangeRate").hasJsonPath())
                .andExpect(jsonPath("exchangeRate.quotedCurrency").value("PHP"))
                .andExpect(jsonPath("amount.value").isNumber()
                );
    }
}