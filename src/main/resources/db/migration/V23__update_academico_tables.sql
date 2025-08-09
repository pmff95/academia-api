DROP TABLE grade_horario;

CREATE TABLE turma_disciplina (
                               id BIGSERIAL PRIMARY KEY,
                               uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
                               turma_id BIGINT NOT NULL,
                               professor_id BIGINT NOT NULL,
                               disciplina_id BIGINT NOT NULL,
                               criado_em TIMESTAMP DEFAULT NOW(),
                               atualizado_em TIMESTAMP DEFAULT NOW(),
                               version INT NOT NULL DEFAULT 0,

                               CONSTRAINT fk_grade_horario_turma
                                   FOREIGN KEY (turma_id) REFERENCES turma(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT fk_grade_horario_professor
                                   FOREIGN KEY (professor_id) REFERENCES professor(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT fk_grade_horario_disciplina
                                   FOREIGN KEY (disciplina_id) REFERENCES disciplina(id)
                                       ON DELETE CASCADE
);

CREATE TABLE itinerario_formativo (
                                      id BIGSERIAL PRIMARY KEY,
                                      uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
                                      criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
                                      atualizado_em TIMESTAMP DEFAULT NOW(),

                                      nome VARCHAR(255),
                                      descricao TEXT
);


CREATE TABLE grupo_itinerario (
                                  id BIGSERIAL PRIMARY KEY,
                                  uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
                                  criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
                                  atualizado_em TIMESTAMP DEFAULT NOW(),

                                  nome VARCHAR(255),
                                  ano_letivo INTEGER,

                                  itinerario_id BIGINT NOT NULL,

                                  CONSTRAINT fk_grupo_itinerario_itinerario
                                      FOREIGN KEY (itinerario_id) REFERENCES itinerario_formativo(id) ON DELETE CASCADE
);


CREATE TABLE aluno_grupo_itinerario (
                                        id BIGSERIAL PRIMARY KEY,
                                        uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
                                        criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
                                        atualizado_em TIMESTAMP DEFAULT NOW(),

                                        aluno_id BIGINT NOT NULL,
                                        grupo_id BIGINT NOT NULL,

                                        CONSTRAINT fk_aluno_grupo_itinerario_aluno
                                            FOREIGN KEY (aluno_id) REFERENCES aluno(id) ON DELETE CASCADE,

                                        CONSTRAINT fk_aluno_grupo_itinerario_grupo
                                            FOREIGN KEY (grupo_id) REFERENCES grupo_itinerario(id) ON DELETE CASCADE
);

CREATE TABLE disciplina_grupo_itinerario (
                                            id BIGSERIAL PRIMARY KEY,
                                            uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
                                            criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
                                            atualizado_em TIMESTAMP DEFAULT NOW(),

                                            grupo_id BIGINT NOT NULL,
                                            disciplina_id BIGINT NOT NULL,
                                            professor_id BIGINT NOT NULL,

                                            CONSTRAINT fk_disciplina_grupo_itinerario_grupo
                                                FOREIGN KEY (grupo_id) REFERENCES grupo_itinerario(id) ON DELETE CASCADE,

                                            CONSTRAINT fk_disciplina_grupo_itinerario_disciplina
                                                FOREIGN KEY (disciplina_id) REFERENCES disciplina(id) ON DELETE CASCADE,

                                            CONSTRAINT fk_disciplina_grupo_itinerario_professor
                                                FOREIGN KEY (professor_id) REFERENCES professor(id) ON DELETE CASCADE
);

CREATE TABLE aula_horario (
                              id BIGSERIAL PRIMARY KEY,
                              uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
                              dia VARCHAR(20) CHECK (dia IN ('SEGUNDA', 'TERCA', 'QUARTA', 'QUINTA', 'SEXTA')) NOT NULL,
                              inicio TIME NOT NULL,
                              fim TIME NOT NULL,
                              turma_disciplina_id BIGINT,
                              disciplina_grupo_itinerario_id BIGINT,
                              criado_em TIMESTAMP DEFAULT NOW(),
                              atualizado_em TIMESTAMP DEFAULT NOW(),
                              version INT NOT NULL DEFAULT 0,

                              CONSTRAINT fk_grade_horario_turma_disciplina
                                  FOREIGN KEY (turma_disciplina_id) REFERENCES turma_disciplina(id)
                                      ON DELETE CASCADE,

                              CONSTRAINT fk_grade_horario_disciplina_grupo_itinerario
                                  FOREIGN KEY (disciplina_grupo_itinerario_id) REFERENCES disciplina_grupo_itinerario(id)
                                      ON DELETE CASCADE
);


