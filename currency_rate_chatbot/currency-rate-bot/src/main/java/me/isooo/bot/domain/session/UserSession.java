package me.isooo.bot.domain.session;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.*;

@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    @CreationTimestamp
    private LocalDateTime creationTime;
    @UpdateTimestamp
    private LocalDateTime lastAccessedTime;

    public UserSession(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return userId + "_" + creationTime;
    }

    public void setLastAccessedTime() {
        this.lastAccessedTime = LocalDateTime.now();
    }
}
