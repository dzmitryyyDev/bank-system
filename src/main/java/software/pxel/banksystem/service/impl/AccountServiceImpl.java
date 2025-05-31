package software.pxel.banksystem.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.pxel.banksystem.dao.repository.AccountRepository;
import software.pxel.banksystem.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger log = LogManager.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;

    private static final long BATCH_SIZE = 100000L;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Запускается каждые 30 секунд
     * Обновляет балансы пакетами по BATCH_SIZE
     */
    @Scheduled(fixedRate = 30000)
    public void updateBalancesBulk() {
        Long minId = accountRepository.findMinId();
        Long maxId = accountRepository.findMaxId();

        if (minId == null || maxId == null) {
            log.warn("No accounts found to update");
            return;
        }

        long startId = minId;
        long endId;

        long startTime = System.currentTimeMillis();

        while (startId <= maxId) {
            endId = Math.min(startId + BATCH_SIZE - 1, maxId);
            accountRepository.bulkUpdateBalancesInRange(startId, endId);
            startId = endId + 1;
        }

        long endTime = System.currentTimeMillis();

        log.info("Total balance update completed in {} ms", (endTime - startTime));
    }
}
