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

---

## Documentação da API

Esta aplicação possui uma **documentação interativa** disponível via Swagger, que pode ser acessada diretamente pela web, sem necessidade de instalar ou rodar a aplicação localmente.

## Swagger UI (Web)
[https://dev-matheuslf-desafio-inscritos.onrender.com/swagger-ui/index.html](https://dev-matheuslf-desafio-inscritos.onrender.com/swagger-ui/index.html)

> ⚠️ Ao acessar o link, o serviço pode demorar um pouco para iniciar. Por favor, aguarde alguns instantes até que a documentação esteja totalmente carregada.

## Swagger UI (Local)
Caso você esteja rodando a aplicação localmente, a documentação estará disponível em:  
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

# Como usar local

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

