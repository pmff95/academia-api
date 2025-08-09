ALTER TABLE controle_matricula DROP CONSTRAINT controle_matricula_pkey;

ALTER TABLE controle_matricula DROP COLUMN letra_atual;

ALTER TABLE controle_matricula
    ADD COLUMN tipo VARCHAR(35) NOT NULL;

ALTER TABLE controle_matricula
    ADD CONSTRAINT pk_controle_matricula PRIMARY KEY (ano, tipo, escola_id);

ALTER TABLE controle_matricula
    ADD CONSTRAINT chk_tipo_matricula_valid
        CHECK (tipo IN ('ALUNO', 'PROFESSOR', 'FUNCIONARIO'));
