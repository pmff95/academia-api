-- V49__alter_table_turma_with_constraint.sql
-- Descrição: Adiciona as colunas 'serie' e 'ano_letivo' na tabela 'turma'
-- e inclui constraint de validação para o campo 'serie' e 'ano_letivo'

-- Adiciona coluna 'serie' como VARCHAR (Enum convertido em string no banco)
ALTER TABLE turma
    ADD COLUMN serie VARCHAR(30);

-- Adiciona coluna 'ano_letivo' como inteiro
ALTER TABLE turma
    ADD COLUMN ano_letivo INTEGER;

-- Constraint para garantir que 'serie' tenha valores válidos do enum Java
ALTER TABLE turma
    ADD CONSTRAINT chk_turma_serie CHECK (
        serie IN (
                  'BERÇARIO_I',
                  'BERÇARIO_II',
                  'MATERNAL_I',
                  'MATERNAL_II',
                  'PRE_I',
                  'PRE_II',
                  'PRIMEIRO_ANO',
                  'SEGUNDO_ANO',
                  'TERCEIRO_ANO',
                  'QUARTO_ANO',
                  'QUINTO_ANO',
                  'SEXTO_ANO',
                  'SETIMO_ANO',
                  'OITAVO_ANO',
                  'NONO_ANO',
                  'PRIMEIRO_MEDIO',
                  'SEGUNDO_MEDIO',
                  'TERCEIRO_MEDIO'
            )
        );
