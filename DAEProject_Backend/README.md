# DAE Project – Backend (Scientific Publications Platform)

REST API for a platform where researchers share scientific publications. Built for the **Enterprise Application Development (Desenvolvimento de Aplicações Empresariais, DAE)** course of the Computer Engineering degree at the Polytechnic of Leiria (ESTG, 2025/26).

The frontend lives in a separate repository: **DAEProject_Frontend** (Nuxt 4).

## Features

- **Authentication**: JWT login and logout, password recovery by e-mail, password change.
- **Roles**: `collaborator`, `moderator` and `administrator`, enforced by JAX-RS authentication and authorization filters.
- **Publications**: create, edit and delete publications with an attached file (conference paper, thesis, article, report...), authors, abstract and tags; file download; hide and unhide (moderation); up and down votes.
- **Comments**: comment on publications, edit comments, and hide them (moderation).
- **Tags**: tag CRUD, plus subscribing and unsubscribing to tags.
- **History and auditing**: a change history per publication and an activity history per user.
- **Administration**: user management (create, edit, delete, list).
- **E-mail**: notifications sent through a fake SMTP server in development.

## Tech stack

- Jakarta EE (EJB, JPA/Hibernate, JAX-RS/RESTEasy, Bean Validation)
- WildFly 27 (JDK 17)
- PostgreSQL
- JJWT for tokens
- Maven, Docker and Docker Compose

## API overview

Base URL: `http://localhost:8080/academics/api/v1`

| Resource        | Main endpoints                                                                 |
|-----------------|--------------------------------------------------------------------------------|
| `/auth`         | `POST /login`, `POST /logout`, `POST /recover-password`                        |
| `/users`        | `GET/PATCH /me`, `PUT /me/password`, `GET /me/comments`, `GET /me/history`, `GET /{id}/history` |
| `/publications` | `GET /`, `GET /me`, `GET /{id}`, `POST /`, `PATCH /{id}`, `DELETE /{id}`, `PATCH /{id}/hide`, `PUT /{id}/vote`, `GET /{id}/history`, `GET/POST /{id}/comments`, `GET /download/{filename}` |
| `/comments`     | `PUT /{id}`, `PATCH /{id}` (hide)                                              |
| `/tags`         | `GET /`, `POST /`, `PATCH /{id}`, `DELETE /{id}`, `GET /me`, `POST /{id}/subscribe`, `POST /{id}/unsubscribe` |
| `/admin`        | `GET /users`, `GET /users/{id}`, `POST /users`, `PATCH /users/{id}`, `DELETE /users/{id}` |

Example requests are in [`sample-requests.http`](sample-requests.http).

## Project structure

```
src/main/java/pt/ipleiria/estg/dei/ei/dae/project/
├── dtos/        # Data transfer objects
├── ejbs/        # Business logic (stateless beans) + ConfigBean (seed data)
├── entities/    # JPA entities
├── enums/       # Roles, file types, vote types...
├── providers/   # CORS filter
├── security/    # JWT issuing, hashing, auth filters
└── ws/          # REST resources
```

## Running locally

Requirements: Docker (with Compose), Maven, JDK 17 and `make`.

1. Create the environment file from the template and fill in the values:

   ```bash
   cp src/.env.exemple .env
   ```

   `APPLICATION_NAME` must be `academics` (the WAR name), and `DATASOURCE_JNDI` must be `java:/AcademicsDS`. Use `db` as `DB_HOST`.

2. Start PostgreSQL, the fake SMTP server and WildFly:

   ```bash
   make up
   ```

3. Build the WAR and deploy it to WildFly:

   ```bash
   make deploy      # or: make monitor  (deploy + follow logs)
   ```

Other useful targets: `make logs`, `make sql` (psql shell), `make mails` (look at sent e-mails), `make down`.

On startup, `ConfigBean` seeds demo users (one for each role) and some tags. See [`ConfigBean.java`](src/main/java/pt/ipleiria/estg/dei/ei/dae/project/ejbs/ConfigBean.java).

## Authors

- Eduardo Amado
- Fernando Videira
- Afonso Sousa

Academic project. Polytechnic of Leiria, School of Technology and Management (ESTG).
