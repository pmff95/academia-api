package com.example.demo.common.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Remove revisões mais antigas que 90 dias.
 * O FK ON DELETE CASCADE criado pelo Envers garante que,
 * ao apagar linhas da audit.revinfo, todas as *_aud ligadas
 * sejam limpas automaticamente.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuditCleanupJob {

    private final JdbcTemplate jdbc;

    /**
     * roda todo dia às 02:30 (AM)
     */
    @Scheduled(cron = "0 30 2 * * *")         // ajuste o horário se quiser
    @Transactional
    public void purgeOldRevisions() {
        int deleted = jdbc.update("""
                    DELETE FROM audit.revinfo
                    WHERE to_timestamp(timestamp / 1000) < now() - interval '90 days'
                """);
        log.info("AuditCleanupJob ⇒ {} revisões antigas removidas", deleted);
    }
}
