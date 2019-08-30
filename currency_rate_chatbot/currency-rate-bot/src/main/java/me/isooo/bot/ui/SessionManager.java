package me.isooo.bot.ui;

import lombok.extern.slf4j.*;
import me.isooo.bot.domain.session.*;
import me.isooo.bot.support.config.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;

import java.time.*;

@Slf4j
@Component
public class SessionManager {
    private final SessionRepository sessionRepository;
    private final SessionInactiveIntervalConfig sessionInactiveIntervalConfig;

    public SessionManager(SessionRepository sessionRepository, SessionInactiveIntervalConfig sessionInactiveIntervalConfig) {
        this.sessionRepository = sessionRepository;
        this.sessionInactiveIntervalConfig = sessionInactiveIntervalConfig;
    }

    public String getUserSession(String userId) {
        UserSession userSession = sessionRepository.findFirstByUserIdOrderByIdDesc(userId);
        if (StringUtils.isEmpty(userSession) || isInvalidate(userSession)) {
            userSession = new UserSession(userId);
        }
        userSession.setLastAccessedTime();
        log.info("sessionId : {}, lastAccessedTime : {}", userSession.getSessionId(), userSession.getLastAccessedTime());
        return sessionRepository.save(userSession).getSessionId();
    }

    private boolean isInvalidate(UserSession userSession) {
        final Duration parse = Duration.parse(this.sessionInactiveIntervalConfig.getMaxInactiveInterval());
        return LocalDateTime.now()
                .minus(parse)
                .isAfter(userSession.getLastAccessedTime());
    }
}
