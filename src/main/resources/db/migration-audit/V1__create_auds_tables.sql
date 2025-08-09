/* ====================================================================== */
/*  V1__create_auds_tables.sql                                            */
/*  Gera estrutura de auditoria (Hibernate Envers) no schema  "audit"     */
/* ====================================================================== */

-- 1. Cria (ou garante) o schema dedicado à auditoria
CREATE SCHEMA IF NOT EXISTS audit;
CREATE SCHEMA IF NOT EXISTS qaaudit;

-- 2. Tabela de revisões usada pelo Envers
CREATE TABLE revinfo (
                         id SERIAL PRIMARY KEY,
                         timestamp BIGINT NOT NULL,
                         nome VARCHAR(255) NOT NULL,
                         usuario_id BIGINT,
                         escola_id BIGINT
);

CREATE SEQUENCE IF NOT EXISTS audit.revinfo_seq START WITH 1 INCREMENT BY 50;


-------------------------------------------------------------------------------
-- A partir daqui, para cada tabela do schema público criamos a respectiva
-- tabela *_aud com as colunas originais + (rev, revtype) e PK composta (id,rev)
-------------------------------------------------------------------------------

/* ========================  ESCOLA  ===================================== */
CREATE TABLE escola_aud (
                                  id              BIGINT      NOT NULL,
                                  rev             INT         NOT NULL,
                                  revtype         SMALLINT,
                                  uuid            UUID,
                                  nome            VARCHAR(255),
                                  cnpj            VARCHAR(14),
                                  status          VARCHAR(20),
                                  payment_secret  VARCHAR(255),
                                  version         INTEGER,
                                  criado_em       TIMESTAMP,
                                  atualizado_em   TIMESTAMP,
                                  PRIMARY KEY (id, rev),
                                  CONSTRAINT fk_escola_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  USUARIO  ==================================== */
CREATE TABLE usuario_aud (
                                   id                   BIGINT      NOT NULL,
                                   rev                  INT         NOT NULL,
                                   revtype              SMALLINT,
                                   uuid                 UUID,
                                   escola_id            BIGINT,
                                   nome                 VARCHAR(255),
                                   email                VARCHAR(255),
                                   nickname             VARCHAR(255),
                                   matricula            VARCHAR(255),
                                   senha                VARCHAR(100),
                                   cpf                  VARCHAR(11),
                                   telefone             VARCHAR(20),
                                   metodo_autenticacao  VARCHAR(50),
                                   perfil               VARCHAR(50),
                                   status               VARCHAR(20),
                                   foto                 VARCHAR(255),
                                   primeiro_acesso      BOOLEAN,
                                   pai                  VARCHAR(255),
                                   mae                  VARCHAR(255),
                                   version              INTEGER,
                                   criado_em            TIMESTAMP,
                                   atualizado_em        TIMESTAMP,
                                   ultimo_acesso        TIMESTAMP,
                                   PRIMARY KEY (id, rev),
                                   CONSTRAINT fk_usuario_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  ALUNO  ====================================== */
CREATE TABLE aluno_aud (
                                 id            BIGINT      NOT NULL,
                                 rev           INT         NOT NULL,
                                 revtype       SMALLINT,
                                 matricula     VARCHAR(255),
                                 foto          VARCHAR(255),
                                 criado_em     TIMESTAMP,
                                 atualizado_em TIMESTAMP,
                                 version       INTEGER,
                                 PRIMARY KEY (id, rev),
                                 CONSTRAINT fk_aluno_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  RESPONSAVEL_ALUNO  ========================= */
CREATE TABLE responsavel_aluno_aud (
                                             id              BIGINT      NOT NULL,
                                             rev             INT         NOT NULL,
                                             revtype         SMALLINT,
                                             responsavel_id  BIGINT,
                                             aluno_id        BIGINT,
                                             grau_parentesco VARCHAR(30),
                                             version         INTEGER,
                                             criado_em       TIMESTAMP,
                                             PRIMARY KEY (id, rev),
                                             CONSTRAINT fk_resp_aluno_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  CATEGORIA_PRODUTO  ========================= */
CREATE TABLE categoria_produto_aud (
                                             id            BIGINT      NOT NULL,
                                             rev           INT         NOT NULL,
                                             revtype       SMALLINT,
                                             uuid          UUID,
                                             nome          VARCHAR(255),
                                             status        VARCHAR(50),
                                             escola_id     BIGINT,
                                             version       INTEGER,
                                             criado_em     TIMESTAMP,
                                             atualizado_em TIMESTAMP,
                                             PRIMARY KEY (id, rev),
                                             CONSTRAINT fk_cat_prod_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  PRODUTO  ==================================== */
CREATE TABLE produto_aud (
                                   id                 BIGINT      NOT NULL,
                                   rev                INT         NOT NULL,
                                   revtype            SMALLINT,
                                   uuid               UUID,
                                   escola_id          BIGINT,
                                   nome               VARCHAR(255),
                                   foto               VARCHAR(255),
                                   preco              NUMERIC(10,2),
                                   departamento       VARCHAR(50),
                                   categoria_id       BIGINT,
                                   status             VARCHAR(20),
                                   quantidade_vendida BIGINT,
                                   version            INTEGER,
                                   criado_em          TIMESTAMP,
                                   atualizado_em      TIMESTAMP,
                                   PRIMARY KEY (id, rev),
                                   CONSTRAINT fk_produto_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  PEDIDO  ===================================== */
CREATE TABLE pedido_aud (
                                  id            BIGINT      NOT NULL,
                                  rev           INT         NOT NULL,
                                  revtype       SMALLINT,
                                  uuid          UUID,
                                  escola_id     BIGINT,
                                  comprador_id  BIGINT,
                                  vendedor_id   BIGINT,
                                  valor_total   NUMERIC(15,2),
                                  status        VARCHAR(20),
                                  criado_em     TIMESTAMP,
                                  atualizado_em TIMESTAMP,
                                  version       INTEGER,
                                  PRIMARY KEY (id, rev),
                                  CONSTRAINT fk_pedido_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  ITEM_PEDIDO  ================================ */
CREATE TABLE item_pedido_aud (
                                       id                BIGINT      NOT NULL,
                                       rev               INT         NOT NULL,
                                       revtype           SMALLINT,
                                       uuid              UUID,
                                       pedido_id         BIGINT,
                                       produto_id        BIGINT,
                                       descricao_produto VARCHAR(255),
                                       quantidade        INTEGER,
                                       valor_unitario    NUMERIC(15,2),
                                       valor_total       NUMERIC(15,2),
                                       version           INTEGER,
                                       criado_em         TIMESTAMP,
                                       atualizado_em     TIMESTAMP,
                                       PRIMARY KEY (id, rev),
                                       CONSTRAINT fk_item_ped_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  DISCIPLINA  ================================ */
CREATE TABLE disciplina_aud (
                                      id            BIGINT      NOT NULL,
                                      rev           INT         NOT NULL,
                                      revtype       SMALLINT,
                                      nome          VARCHAR(255),
                                      uuid          UUID,
                                      criado_em     TIMESTAMP,
                                      atualizado_em TIMESTAMP,
                                      version       INTEGER,
                                      status        VARCHAR(20),
                                      PRIMARY KEY (id, rev),
                                      CONSTRAINT fk_disc_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  PROFESSOR  ================================= */
CREATE TABLE professor_aud (
                                     id            BIGINT      NOT NULL,
                                     rev           INT         NOT NULL,
                                     revtype       SMALLINT,
                                     foto          VARCHAR(255),
                                     criado_em     TIMESTAMP,
                                     atualizado_em TIMESTAMP,
                                     version       INTEGER,
                                     PRIMARY KEY (id, rev),
                                     CONSTRAINT fk_prof_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  PROFESSOR_DISCIPLINA  ====================== */
CREATE TABLE professor_disciplina_aud (
                                                professor_id  BIGINT      NOT NULL,
                                                disciplina_id BIGINT      NOT NULL,
                                                rev           INT         NOT NULL,
                                                revtype       SMALLINT,
                                                PRIMARY KEY (professor_id, disciplina_id, rev),
                                                CONSTRAINT fk_prof_disc_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  HORARIO_DISPONIVEL  ======================== */
CREATE TABLE horario_disponivel_aud (
                                              id              BIGINT      NOT NULL,
                                              rev             INT         NOT NULL,
                                              revtype         SMALLINT,
                                              professor_id    BIGINT,
                                              dia_semana      VARCHAR(20),
                                              turno           VARCHAR(20),
                                              horario_inicio  TIME,
                                              horario_fim     TIME,
                                              criado_em       TIMESTAMP,
                                              atualizado_em   TIMESTAMP,
                                              version         INTEGER,
                                              uuid            UUID,
                                              PRIMARY KEY (id, rev),
                                              CONSTRAINT fk_hor_disp_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  CARTEIRA  ================================== */
CREATE TABLE carteira_aud (
                                    id            BIGINT      NOT NULL,
                                    rev           INT         NOT NULL,
                                    revtype       SMALLINT,
                                    uuid          UUID,
                                    aluno_id      BIGINT,
                                    professor_id  BIGINT,
                                    saldo         NUMERIC(19,2),
                                    version       INTEGER,
                                    criado_em     TIMESTAMP,
                                    atualizado_em TIMESTAMP,
                                    PRIMARY KEY (id, rev),
                                    CONSTRAINT fk_carteira_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  CARTAO_CARTEIRA  =========================== */
CREATE TABLE cartao_carteira_aud (
                                           id            BIGINT      NOT NULL,
                                           rev           INT         NOT NULL,
                                           revtype       SMALLINT,
                                           uuid          UUID,
                                           carteira_id   BIGINT,
                                           numero        VARCHAR(255),
                                           senha         VARCHAR(255),
                                           status        VARCHAR(20),
                                           version       INTEGER,
                                           criado_em     TIMESTAMP,
                                           atualizado_em TIMESTAMP,
                                           PRIMARY KEY (id, rev),
                                           CONSTRAINT fk_cartao_cart_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  TRANSACAO  ================================= */
CREATE TABLE transacao_aud (
                                     id             BIGINT      NOT NULL,
                                     rev            INT         NOT NULL,
                                     revtype        SMALLINT,
                                     uuid           UUID,
                                     carteira_id    BIGINT,
                                     valor          NUMERIC(19,2),
                                     tipo_transacao VARCHAR,
                                     usuario_id     BIGINT,
                                     pedido_id      BIGINT,
                                     version        INTEGER,
                                     criado_em      TIMESTAMP,
                                     atualizado_em  TIMESTAMP,
                                     PRIMARY KEY (id, rev),
                                     CONSTRAINT fk_transacao_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  ESCOLA_ENDERECO  =========================== */
CREATE TABLE escola_endereco_aud (
                                           id          BIGINT      NOT NULL,
                                           rev         INT         NOT NULL,
                                           revtype     SMALLINT,
                                           escola_id   BIGINT,
                                           telefone    VARCHAR(20),
                                           cep         VARCHAR(10),
                                           endereco    VARCHAR(100),
                                           numero      VARCHAR(10),
                                           bairro      VARCHAR(50),
                                           complemento VARCHAR(50),
                                           cidade      VARCHAR(50),
                                           estado      VARCHAR(2),
                                           version     INTEGER,
                                           PRIMARY KEY (id, rev),
                                           CONSTRAINT fk_escola_end_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  ESCOLA_FINANCEIRO  ========================= */
CREATE TABLE escola_financeiro_aud (
                                             id              BIGINT      NOT NULL,
                                             rev             INT         NOT NULL,
                                             revtype         SMALLINT,
                                             escola_id       BIGINT,
                                             dia_pagamento   INTEGER,
                                             dia_recebimento INTEGER,
                                             version         INTEGER,
                                             PRIMARY KEY (id, rev),
                                             CONSTRAINT fk_escola_fin_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  SERIE  ===================================== */
CREATE TABLE serie_aud (
                                 id            BIGINT      NOT NULL,
                                 rev           INT         NOT NULL,
                                 revtype       SMALLINT,
                                 uuid          UUID,
                                 nome          VARCHAR(255),
                                 turno         VARCHAR(20),
                                 criado_em     TIMESTAMP,
                                 atualizado_em TIMESTAMP,
                                 version       INTEGER,
                                 PRIMARY KEY (id, rev),
                                 CONSTRAINT fk_serie_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  GRADE_HORARIO  ============================= */
CREATE TABLE grade_horario_aud (
                                         id            BIGINT      NOT NULL,
                                         rev           INT         NOT NULL,
                                         revtype       SMALLINT,
                                         uuid          UUID,
                                         dia           VARCHAR(20),
                                         inicio        TIME,
                                         fim           TIME,
                                         serie_id      BIGINT,
                                         professor_id  BIGINT,
                                         disciplina_id BIGINT,
                                         criado_em     TIMESTAMP,
                                         atualizado_em TIMESTAMP,
                                         version       INTEGER,
                                         PRIMARY KEY (id, rev),
                                         CONSTRAINT fk_grd_hor_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  ENDERECO (pessoa)  ========================= */
CREATE TABLE endereco_aud (
                                    id            BIGINT      NOT NULL,
                                    rev           INT         NOT NULL,
                                    revtype       SMALLINT,
                                    uuid          UUID,
                                    cep           VARCHAR(9),
                                    endereco      VARCHAR(255),
                                    numero        VARCHAR(20),
                                    cidade        VARCHAR(100),
                                    estado        VARCHAR(2),
                                    usuario_id    BIGINT,
                                    criado_em     TIMESTAMP,
                                    atualizado_em TIMESTAMP,
                                    PRIMARY KEY (id, rev),
                                    CONSTRAINT fk_endereco_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  TURMA  ===================================== */
CREATE TABLE turma_aud (
                                 id            BIGINT      NOT NULL,
                                 rev           INT         NOT NULL,
                                 revtype       SMALLINT,
                                 uuid          UUID,
                                 nome          VARCHAR(255),
                                 serie_id      BIGINT,
                                 criado_em     TIMESTAMP,
                                 atualizado_em TIMESTAMP,
                                 version       INTEGER,
                                 PRIMARY KEY (id, rev),
                                 CONSTRAINT fk_turma_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  ESCOLA_MODULO  ============================ */
CREATE TABLE escola_modulo_aud (
                                         id             BIGINT      NOT NULL,
                                         rev            INT         NOT NULL,
                                         revtype        SMALLINT,
                                         escola_id      BIGINT,
                                         modulo         VARCHAR(50),
                                         ativo          BOOLEAN,
                                         data_ativacao  DATE,
                                         data_expiracao DATE,
                                         PRIMARY KEY (id, rev),
                                         CONSTRAINT fk_escola_mod_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  PAGAMENTO  ================================ */
CREATE TABLE pagamento_aud (
                                     id            BIGINT      NOT NULL,
                                     rev           INT         NOT NULL,
                                     revtype       SMALLINT,
                                     uuid          UUID,
                                     usuario_id    BIGINT,
                                     mp_id         VARCHAR(255),
                                     status        VARCHAR(255),
                                     data          TIMESTAMP,
                                     criado_em     TIMESTAMP,
                                     atualizado_em TIMESTAMP,
                                     PRIMARY KEY (id, rev),
                                     CONSTRAINT fk_pagto_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  PAGAMENTO_ITEM  =========================== */
CREATE TABLE pagamento_item_aud (
                                          id            BIGINT      NOT NULL,
                                          rev           INT         NOT NULL,
                                          revtype       SMALLINT,
                                          uuid          UUID,
                                          pagamento_id  BIGINT,
                                          aluno_id      BIGINT,
                                          tipo          VARCHAR(50),
                                          titulo        VARCHAR(255),
                                          criado_em     TIMESTAMP,
                                          atualizado_em TIMESTAMP,
                                          PRIMARY KEY (id, rev),
                                          CONSTRAINT fk_pagto_item_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  ALUNO_TURMA  ============================== */
CREATE TABLE aluno_turma_aud (
                                       turma_id BIGINT NOT NULL,
                                       aluno_id BIGINT NOT NULL,
                                       rev      INT    NOT NULL,
                                       revtype  SMALLINT,
                                       PRIMARY KEY (turma_id, aluno_id, rev),
                                       CONSTRAINT fk_aluno_turma_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ========================  CONTROLE_MATRICULA  ======================= */
CREATE TABLE controle_matricula_aud (
                                              ano           INTEGER     NOT NULL,
                                              rev           INT         NOT NULL,
                                              revtype       SMALLINT,
                                              letra_atual   VARCHAR(1),
                                              numero_atual  INTEGER,
                                              escola_id     BIGINT,
                                              uuid          UUID,
                                              criado_em     TIMESTAMP,
                                              atualizado_em TIMESTAMP,
                                              PRIMARY KEY (ano, rev),
                                              CONSTRAINT fk_ctrl_matr_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

/* ====================================================================== */
/*  Fim do arquivo V1__create_auds_tables.sql                              */
/* ====================================================================== */
