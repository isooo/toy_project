package me.isooo.bot.batch;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.*;
import java.time.*;

@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class BatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime executedTime;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private BatchStatus status;

    public BatchHistory(BatchStatus status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return this.status == BatchStatus.SUCCESS;
    }
}
