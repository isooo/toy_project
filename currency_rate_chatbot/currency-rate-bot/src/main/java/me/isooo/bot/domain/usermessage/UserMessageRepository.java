package me.isooo.bot.domain.usermessage;

import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {
    Optional<UserMessage> findFirstBySessionIdAndMessageTypeOrderByIdDesc(String sessionId, MessageType messageType);
}
