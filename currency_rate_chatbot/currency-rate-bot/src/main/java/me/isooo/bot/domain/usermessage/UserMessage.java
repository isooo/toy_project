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
    private String sessionId;
    private String message;

    @Enumerated(value = EnumType.STRING)
    private MessageType messageType;

    public UserMessage(String sessionId, String userMessage) {
        this.sessionId = sessionId;
        this.message = userMessage;
        this.messageType = MessageType.getType(userMessage);
    }
}
