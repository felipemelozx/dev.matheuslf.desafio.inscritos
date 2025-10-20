# Desafio Técnico – Sistema de Gestão de Projetos e Demandas

---

Este projeto é a resolução do desafio proposto pelo professor do professor [Matheus Leandro](https://github.com/matheuslf) disponível neste [repositório](https://github.com/matheuslf/dev.matheuslf.desafio.inscritos).

# Tecnologias Utilizadas

---

- Java 17+
- Spring Boot 3.5.6
- Spring Security
- Auth0 (JWT)
- Spring Data JPA
- Flyway (migrações de banco)
- PostgreSQL
- H2 Database (para testes)
- Spring Validation
- Spring Web
- Swagger / OpenAPI (springdoc-openapi)

# Como usar

---

## Pré-requisitos
- Java 17+
- Maven 3.9+
- PostgreSQL (rodando localmente)
- docker compose

---

## Clonando o Projeto

```bash
git clone https://github.com/matheuslf/dev.matheuslf.desafio.inscritos
cd dev.matheuslf.desafio.inscritos
```

## Rode o docker compose
```bash
docker compose up -d
```

## Rode o projeto
```bash
mvn spring-boot:run
```

## Documentação da API

---

Esta API possui documentação interativa via Swagger.

- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Endpoints principais

---

#### Autenticação
- `POST /auth/register` – Registrar novo usuário
- `POST /auth/login` – Fazer login e obter token JWT

#### Projetos
- `GET /projects` – Listar projetos
- `POST /projects` – Criar novo projeto

#### Tarefas
- `GET /tasks` – Listar tarefas
- `POST /tasks` – Criar nova tarefa
- `PUT /tasks/{id}/status` – Atualizar status da tarefa
- `DELETE /tasks/{id}` – Excluir tarefa

