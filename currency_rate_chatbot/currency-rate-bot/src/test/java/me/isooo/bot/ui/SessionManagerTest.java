package me.isooo.bot.ui;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import java.time.*;

import static org.assertj.core.api.Assertions.*;

@DisplayName("SessionManager 테스트")
@SpringBootTest
class SessionManagerTest {
    @Autowired
    private SessionManager sessionManager;

    private final String userId = "test001";

    @Test
    @DisplayName("처음 진입한 유저의 세션 생성 테스트")
    void sessionCreateTest() {
        // given
        // when
        final String sessionId = sessionManager.getUserSession(userId);

        // then
        assertThat(sessionId.startsWith(userId + "_" + LocalDateTime.now().withNano(0)));
    }

    @Test
    @DisplayName("유효 기간 내 진입한 유저의 세션 유지 테스트")
    void sessionMaintainTest() throws InterruptedException {
        // given
        final String sessionId_old = sessionManager.getUserSession(userId);

        // when
        Thread.sleep(2000);
        final String sessionId_new = sessionManager.getUserSession(userId);

        // then
        assertThat(sessionId_old).isEqualTo(sessionId_new);
    }

    @Test
    @DisplayName("유효 기간이 지난 후 진입한 유저의 세션 생성 테스트")
    void sessionCreateAfterInactiveIntervalTest() throws InterruptedException {
        // given
        final String sessionId_old = sessionManager.getUserSession(userId);

        // when
        Thread.sleep(600001);
        final String sessionId_new = sessionManager.getUserSession(userId);

        // then
        assertThat(sessionId_old).isNotEqualTo(sessionId_new);
        assertThat(sessionId_new.startsWith(userId + "_" + LocalDateTime.now().withNano(0)));
    }
}
