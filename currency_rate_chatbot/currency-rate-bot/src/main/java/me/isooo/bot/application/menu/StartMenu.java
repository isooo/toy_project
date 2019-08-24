package me.isooo.bot.application.menu;

import com.linecorp.bot.model.message.*;
import me.isooo.bot.domain.currency.Currency;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class StartMenu implements Menu {
    public static final String Command = "시작";

    @Override
    public List<Message> getMessages(String userId, String userMessage) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("기준 통화 선택하기\n(??? → ???)\n");
        Arrays.stream(Currency.values())
                .map(c -> c.name())
                .forEach(name -> stringBuilder.append("\n" + name));
        final TextMessage textMessage = new TextMessage(stringBuilder.toString());
        return Collections.singletonList(textMessage);
    }
}
