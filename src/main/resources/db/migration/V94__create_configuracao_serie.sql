-- Cria a tabela configuracao_serie com campos de quantidade de avaliacoes e media
CREATE TABLE configuracao_serie (
    id BIGSERIAL PRIMARY KEY,
    escola_id BIGINT NOT NULL REFERENCES escola(id) ON DELETE CASCADE,
    serie VARCHAR(50) NOT NULL,
    tipo_periodo VARCHAR(20) NOT NULL,
    quantidade_avaliacoes INTEGER NOT NULL,
    media NUMERIC(5,2) NOT NULL,
    version INT NOT NULL DEFAULT 0
);

CREATE UNIQUE INDEX uq_configuracao_serie_escola_serie ON configuracao_serie(escola_id, serie);
