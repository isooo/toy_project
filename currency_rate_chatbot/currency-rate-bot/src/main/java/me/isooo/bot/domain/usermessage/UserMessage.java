package me.isooo.bot.domain.usermessage;

import lombok.*;

import javax.persistence.*;

@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class UserMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String message;

    public UserMessage(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }
}
