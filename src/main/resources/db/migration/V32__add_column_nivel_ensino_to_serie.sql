-- Adicionando coluna 'nivel_ensino' na tabela 'serie'
ALTER TABLE serie
    ADD COLUMN IF NOT EXISTS nivel_ensino VARCHAR(50);

ALTER TABLE serie
    ADD CONSTRAINT chk_nivel_ensino
        CHECK (nivel_ensino IN (
                                'MATERNAL',
                                'EDUCACAO_INFANTIL',
                                'FUNDAMENTAL_ANOS_INICIAIS',
                                'FUNDAMENTAL_ANOS_FINAIS',
                                'ENSINO_MEDIO'
            ));
