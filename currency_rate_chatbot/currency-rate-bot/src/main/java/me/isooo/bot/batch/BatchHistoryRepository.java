package me.isooo.bot.batch;

import org.springframework.data.jpa.repository.*;

public interface BatchHistoryRepository extends JpaRepository<BatchHistory, Long> {
    BatchHistory findFirstByOrderByIdDesc();
}