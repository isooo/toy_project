package me.isooo.bot.batch;

import lombok.extern.slf4j.*;
import me.isooo.bot.application.*;
import me.isooo.bot.domain.currency.Currency;
import me.isooo.bot.domain.currency.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;

import javax.annotation.*;
import java.math.*;
import java.util.*;

@Slf4j
@Component
public class CurrencyRateBatch {
    private final CurrencyApi currencyApi;
    private final BatchHistoryRepository batchHistoryRepository;
    private final CurrencyRateRepository currencyRateRepository;

    public CurrencyRateBatch(
            CurrencyApi currencyApi,
            BatchHistoryRepository batchHistoryRepository,
            CurrencyRateRepository currencyRateRepository
    ) {
        this.currencyApi = currencyApi;
        this.batchHistoryRepository = batchHistoryRepository;
        this.currencyRateRepository = currencyRateRepository;
    }

    @Scheduled(cron = "${batch.currency-rate-cron}")
    public BatchStatus execute() {
        batchHistoryRepository.save(new BatchHistory(BatchStatus.START));
        try {
            StopWatch stopWatch = new StopWatch("currency rate batch");
            stopWatch.start("call api");
            final List<CurrencyRate> currencyRates = currencyApi.getRates();
            stopWatch.stop();

            final BatchStatus batchStatus = getBatchResult(currencyRates);
            batchHistoryRepository.save(new BatchHistory(batchStatus));

            stopWatch.start("persistence");
            currencyRateRepository.saveAll(currencyRates);
            stopWatch.stop();

            log.info(stopWatch.prettyPrint());
            return batchStatus;
        } catch (Exception e) {
            log.error("batch execute error: {}", e);
            batchHistoryRepository.save(new BatchHistory(BatchStatus.FAIL));
            return BatchStatus.FAIL;
        }
    }

    @Scheduled(cron = "${batch.result-check-cron}")
    public BatchStatus batchResultCheck() {
        final BatchHistory history = batchHistoryRepository.findFirstByOrderByIdDesc();
        log.info("history: {}", history);
        if (history == null || !(history.isSuccess())) {
            return this.execute();
        }
        return history.getStatus();
    }

    @PostConstruct
    public void init() {
        execute();
    }

    private BatchStatus getBatchResult(List<CurrencyRate> currencyRates) {
        if (CollectionUtils.isEmpty(currencyRates) || isDifferentSize(currencyRates) || isIncludedZero(currencyRates)) {
            return BatchStatus.FAIL;
        }
        return BatchStatus.SUCCESS;
    }

    private boolean isDifferentSize(List<CurrencyRate> currencyRates) {
        return currencyRates.size() != Currency.values().length;
    }

    private boolean isIncludedZero(List<CurrencyRate> currencyRates) {
        return currencyRates.stream()
                .anyMatch(currencyRate -> currencyRate.getRate().compareTo(BigDecimal.ZERO) == 0);
    }
}
