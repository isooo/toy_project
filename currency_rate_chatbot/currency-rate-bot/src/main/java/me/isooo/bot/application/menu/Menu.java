package me.isooo.bot.application.menu;

import com.linecorp.bot.model.message.*;

import java.util.*;

public interface Menu {
    List<Message> getMessages(String userId, String userMessage);
}
