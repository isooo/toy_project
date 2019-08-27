package me.isooo.bot.application.menu;

import org.springframework.stereotype.*;

@Component
public class AllCommand {
    private final StartMenu startMenu;
    private final CurrencyMenu currencyMenu;
    private final AmountMenu amountMenu;
    private final AmountResultMenu amountResultMenu;
    private final GuideMenu guideMenu;

    public AllCommand(
            StartMenu startMenu,
            CurrencyMenu currencyMenu,
            AmountMenu amountMenu,
            AmountResultMenu amountResultMenu,
            GuideMenu guideMenu
    ) {
        this.startMenu = startMenu;
        this.currencyMenu = currencyMenu;
        this.amountMenu = amountMenu;
        this.amountResultMenu = amountResultMenu;
        this.guideMenu = guideMenu;
    }

    public Menu getMenu(String userMessage) {
        if (startMenu.matches(userMessage)) {
            return this.startMenu;
        }
        if (currencyMenu.matches(userMessage)) {
            return this.currencyMenu;
        }
        if (amountMenu.matches(userMessage)) {
            return this.amountMenu;
        }
        if (amountResultMenu.matches(userMessage)) {
            return this.amountResultMenu;
        }
        return this.guideMenu;
    }
}