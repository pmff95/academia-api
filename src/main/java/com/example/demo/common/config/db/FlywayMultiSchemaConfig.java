package com.example.demo.common.config.db;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class FlywayMultiSchemaConfig {

    @Value("${escola.db.scheme}")
    private String appSchema;

    @Value("${escola.db.audit-scheme}")
    private String auditSchema;

    /**
     * MIGRAÇÕES DO SCHEMA AUDIT
     * - cria revinfo + *_aud
     * - usa pasta db/migration-audit
     * - história fica em audit.flyway_schema_history
     */
    @Bean(name = "flywayAudit", initMethod = "migrate")   // ← executa migrate() na criação
    public Flyway flywayAudit(DataSource ds) {
        return Flyway.configure()
                .dataSource(ds)
                .schemas(auditSchema)                        // só audit
                .locations("classpath:db/migration-audit")
                .baselineOnMigrate(true)
                .load();
    }

    /**
     * MIGRAÇÕES DO SCHEMA PUBLIC
     * - cria todas as tabelas da aplicação
     * - usa pasta db/migration
     * - história fica em public.flyway_schema_history
     */
    @Bean(name = "flywayPublic", initMethod = "migrate")
    @Primary                                             // Bean "principal" p/ Spring
    public Flyway flywayPublic(DataSource ds) {
        return Flyway.configure()
                .dataSource(ds)
                .schemas(appSchema)                       // só public
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();
    }
}

