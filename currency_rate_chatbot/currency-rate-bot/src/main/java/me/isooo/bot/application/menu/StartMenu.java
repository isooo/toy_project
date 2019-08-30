package me.isooo.bot.application.menu;

import com.linecorp.bot.model.message.*;
import lombok.extern.slf4j.*;
import me.isooo.bot.domain.currency.Currency;
import org.springframework.stereotype.*;

import java.util.*;

@Slf4j
@Component
public class StartMenu implements Menu {
    public static final String Command = "시작";

    @Override
    public boolean matches(String userMessage) {
        return StartMenu.Command.equals(userMessage);
    }

    @Override
    public List<Message> getMessages(String sessionId, String userMessage) {
        log.info("userId: {}, userMessage: {}", sessionId, userMessage);
        final List<Message> messages = new ArrayList<>();
        final TextMessage textMessage1 = new TextMessage("원하시는 환율에 대한 통화를 차례로 입력해주세요.\n" +
                "e.g. 1유로(EUR) 대비 원화(KRW)가 궁금할 땐\n[EURKRW]를 입력하시면 됩니다 :)");
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("※ 가능한 통화\n");
        Arrays.stream(Currency.values())
                .map(c -> c.name())
                .forEach(name -> stringBuilder.append("\n" + name));
        final TextMessage textMessage2 = new TextMessage(stringBuilder.toString());
        messages.add(textMessage1);
        messages.add(textMessage2);
        return Collections.unmodifiableList(messages);
    }
}
