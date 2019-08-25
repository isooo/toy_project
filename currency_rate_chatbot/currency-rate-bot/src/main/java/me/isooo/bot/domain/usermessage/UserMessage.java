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

    @Enumerated(value = EnumType.STRING)
    private MessageType messageType;

    public UserMessage(String userId, String userMessage) {
        this.userId = userId;
        this.message = userMessage;
        this.messageType = MessageType.getType(userMessage);
    }
}
