# Infrastructure Playground

Ambiente completo para desenvolver, automatizar e realizar deploy de aplicações utilizando Docker, Nginx e uma API Spring Boot segura.

---

## 📚 Visão Geral

```text
Cliente
   │
   ▼
Nginx (porta 80)
   │
   ├── /auth/** ─────► Spring Boot API
   ├── /api/** ──────► Spring Boot API
   │                      │
   │                      ├── PostgreSQL
   │                      └── Redis
   │
   └── Adminer (porta 8080)
```

---

## 🚀 Tecnologias

### Infraestrutura

- Docker
- Docker Compose
- Nginx (Reverse Proxy)
- PostgreSQL 16
- Redis 7
- GitHub Actions (CI)
- Shell Script

### API (`secret-vault`)

- Java 21
- Spring Boot 3
- Spring Security
- JWT
- Criptografia AES
- Spring Data JPA
- PostgreSQL
- Spring Data Redis

---

## ⚙️ Como executar

### Pré-requisitos

- Docker Desktop
- Git

### Clone o projeto

```bash
git clone https://github.com/ericpatricio23/infrastructure-playground.git
cd infrastructure-playground
```

### Inicie o ambiente

```bash
bash scripts/setup.sh
```

Todos os containers serão criados automaticamente.

---

## 🌐 Serviços

| Serviço | Endereço |
|---------|----------|
| API | http://localhost |
| Adminer | http://localhost:8080 |
| PostgreSQL | localhost:5432 |
| Redis | localhost:6379 |

---

## 🔑 Endpoints

### Autenticação

| Método | Endpoint | Descrição |
|---------|----------|-----------|
| POST | `/auth/register` | Cadastro de usuário |
| POST | `/auth/login` | Login e geração de JWT |

### Segredos (JWT obrigatório)

Adicione o seguinte header:

```text
Authorization: Bearer <token>
```

| Método | Endpoint | Descrição |
|---------|----------|-----------|
| POST | `/api/secrets` | Criar segredo |
| GET | `/api/secrets` | Listar segredos |
| GET | `/api/secrets/{id}` | Buscar por ID |
| PUT | `/api/secrets/{id}` | Atualizar |
| DELETE | `/api/secrets/{id}` | Remover |

---

## 📜 Scripts

| Script | Descrição |
|---------|-----------|
| `bash scripts/setup.sh` | Prepara e sobe o ambiente |
| `bash scripts/healthcheck.sh` | Verifica a saúde dos serviços |
| `bash scripts/monitor.sh` | Monitora o uso de recursos |
| `bash scripts/backup.sh` | Gera backup do PostgreSQL |
| `bash scripts/cleanup.sh` | Remove backups antigos |
| `bash scripts/deploy.sh` | Realiza deploy local |

---

## 📁 Estrutura do projeto

```text
infrastructure-playground/
├── app/
│   └── secret-vault/
├── docker/
│   └── nginx/
├── scripts/
├── docs/
├── backups/
├── logs/
└── docker-compose.yml
```

---

## 📖 Documentação

- Arquitetura (`docs/architecture.md`)
- Docker (`docs/docker.md`)
- Linux e Scripts (`docs/linux.md`)
