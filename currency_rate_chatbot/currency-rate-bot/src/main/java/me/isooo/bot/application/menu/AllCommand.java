package me.isooo.bot.application.menu;

import org.springframework.stereotype.*;

@Component
public class AllCommand {
    private final StartMenu startMenu;
    private final CurrencyMenu currencyMenu;
    private final GuideMenu guideMenu;

    public AllCommand(
            StartMenu startMenu,
            CurrencyMenu currencyMenu,
            GuideMenu guideMenu
    ) {
        this.startMenu = startMenu;
        this.currencyMenu = currencyMenu;
        this.guideMenu = guideMenu;
    }

    public Menu getMenu(String userMessage) {
        if (startMenu.Command.equals(userMessage)) {
            return this.startMenu;
        }
        if (currencyMenu.isCurrencyPattern(userMessage)) {
            return this.currencyMenu;
        }
        return this.guideMenu;
    }
}