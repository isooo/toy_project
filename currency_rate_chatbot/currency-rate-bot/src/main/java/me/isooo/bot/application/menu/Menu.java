package me.isooo.bot.application.menu;

import com.linecorp.bot.model.message.*;

import java.util.*;

public interface Menu {
    boolean matches(String userMessage);

    List<Message> getMessages(String sessionId, String userMessage);
}
