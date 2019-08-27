package me.isooo.bot.domain.usermessage;

import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {
    Optional<UserMessage> findFirstByUserIdOrderByIdDesc(String userId);

    Optional<UserMessage> findFirstByUserIdAndMessageTypeOrderByIdDesc(String userId, MessageType messageType);
}
