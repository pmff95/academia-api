@echo off
REM === Carrega variáveis do .env ===
FOR /F "tokens=1,2 delims==" %%A IN ('findstr /V "^#; " .env') DO (
    set "%%A=%%B"
)

REM === Executa flyway:repair ===
mvn flyway:repair -Dflyway.url=%DB_URL% -Dflyway.user=%DB_USER% -Dflyway.password=%DB_PASSWORD% -Dflyway.schemas=%DB_SCHEME%
