# API Demo Spring Boot
[Read in English](README_en.md)

Este projeto é uma API REST com diversos recursos, criada usando Spring Boot e organizada por pacotes que reúne controladores, serviços e modelos de domínio relacionados.

## Pré-requisitos

- **Java 23** (ou JDK compatível)
- **Maven 3.8+** (o repositório fornece o Maven Wrapper `mvnw`)

## Construindo o Projeto

Use o Maven para resolver dependências e empacotar a aplicação:

```bash
./mvnw clean package
```

O comando acima gera o arquivo `target/demo-0.0.1-SNAPSHOT.jar`.

## Executando a Aplicação

Você pode iniciar a aplicação diretamente pelo Maven ou executando o jar gerado:

```bash
# Executar usando o plugin Spring Boot do Maven
./mvnw spring-boot:run

# Ou executar o jar empacotado
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

## Variáveis de Ambiente

A aplicação lê valores sensíveis através das variáveis abaixo:

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`
- `DB_SCHEME`
- `DB_AUDIT_SCHEME`
- `API_KEY`
- `MAIL_FROM`
- `MAIL_HOST`
- `MAIL_PORT`
- `MAIL_USERNAME`
- `MAIL_PASSWORD`
- `CLOUDFLARE_R2_ACCESS_KEY`
- `CLOUDFLARE_R2_SECRET_KEY`
- `CLOUDFLARE_R2_ENDPOINT`
- `CLOUDFLARE_R2_PUBLIC_DOMAIN`
- `CLOUDFLARE_R2_BUCKET`
- `JWT_SECRET`
- `JWT_EXPIRATION`
- `JWT_REFRESH_EXPIRATION`

Durante o desenvolvimento, defina `SPRING_PROFILES_ACTIVE=dev` para que o
arquivo `.env` seja carregado automaticamente.

Exemplo de uso dessas variáveis em `application.properties`:

```properties
db.username=${DB_USER}
db.password=${DB_PASSWORD}
api.key=${API_KEY}
```

Você pode definir esses valores em um arquivo `.env` na raiz do projeto. Um
modelo com exemplos está disponível em `.env.example`.

## Opções de Login

Ao criar um usuário, a aplicação envia um email de boas-vindas contendo sua senha temporária e as opções de login disponíveis. É possível autenticar-se utilizando qualquer um dos seguintes campos registrados no usuário:

- Email
- Matrícula
- Matrícula manual
- Nickname
- CPF
- Telefone

O email também informa todos os perfis (roles) aceitos na aplicação.

## Módulos do Projeto

O código em `src/main/java/com/example/demo` está organizado por funcionalidade. Abaixo está um breve resumo dos principais pacotes:

- **common** – configurações, utilitários, segurança e validações compartilhados.
- **controller** – controladores REST que expõem as rotas de cada módulo.
- **domain** – entidades JPA e modelos de domínio.
- **dto** – objetos de transferência de dados utilizados pelos controladores e serviços.
- **repository** – repositórios Spring Data para operações de persistência.
- **service** – serviços que implementam a lógica de negócio.

Os pacotes específicos por funcionalidade incluem `aluno`, `professor`, `usuario`, `carteira`, `instituicao`, `vendas` e outros. Cada um possui seus próprios controladores, serviços e repositórios.

