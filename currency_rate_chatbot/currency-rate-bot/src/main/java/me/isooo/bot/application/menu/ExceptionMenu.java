package me.isooo.bot.application.menu;

import com.linecorp.bot.model.message.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class ExceptionMenu {
    public List<Message> getMessages() {
        final TextMessage textMessage = new TextMessage("통화를 다시 한 번 확인해주세요 :)");
        return Collections.singletonList(textMessage);
    }
}
