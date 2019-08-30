package me.isooo.bot.domain.session;

import org.springframework.data.jpa.repository.*;

public interface SessionRepository extends JpaRepository<UserSession, Long> {
    UserSession findFirstByUserIdOrderByIdDesc(String userId);
}
