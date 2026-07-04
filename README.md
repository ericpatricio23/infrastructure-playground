# Infrastructure Playground

Ambiente completo para desenvolver, automatizar e fazer deploy de aplicações.
Combina infraestrutura Docker com uma API Spring Boot segura.

## Visao Geral
Cliente
│
▼
Nginx (porta 80)
│
├── /auth/** → Spring Boot (secret-vault)
├── /api/**  → Spring Boot (secret-vault)
│                │
│                ├── PostgreSQL
│                └── Redis
│
└── Adminer (porta 8080)

## Tecnologias

**Infraestrutura**
- Docker e Docker Compose
- Nginx (reverse proxy)
- PostgreSQL 16
- Redis 7
- GitHub Actions (CI)
- Shell Script

**API (secret-vault)**
- Java 21
- Spring Boot 3
- Spring Security + JWT
- Criptografia AES
- Spring Data JPA
- Spring Data Redis

## Como rodar

### Pre-requisitos
- Docker Desktop instalado e rodando
- Git

### Passos

```bash
# Clone o repositorio
git clone https://github.com/ericpatricio23/infrastructure-playground.git
cd infrastructure-playground

# Suba o ambiente completo
bash scripts/setup.sh
```

Aguarde todos os containers subirem. A API sera buildada automaticamente.

## Endpoints

### Autenticacao
POST /auth/register  → Cadastro de usuario
POST /auth/login     → Login e geracao de JWT

### Segredos (requer JWT)
POST   /api/secrets      → Criar segredo
GET    /api/secrets      → Listar segredos
GET    /api/secrets/{id} → Buscar segredo por ID
PUT    /api/secrets/{id} → Atualizar segredo
DELETE /api/secrets/{id} → Remover segredo

## Servicos disponiveis

| Servico    | URL                    |
|------------|------------------------|
| API        | http://localhost       |
| Adminer    | http://localhost:8080  |
| PostgreSQL | localhost:5432         |
| Redis      | localhost:6379         |

## Scripts de automacao

```bash
bash scripts/setup.sh        # Prepara e sobe o ambiente
bash scripts/healthcheck.sh  # Verifica saude dos servicos
bash scripts/monitor.sh      # Monitora uso de recursos
bash scripts/backup.sh       # Gera backup do PostgreSQL
bash scripts/cleanup.sh      # Remove backups antigos
bash scripts/deploy.sh       # Realiza deploy local
```

## Estrutura do projeto
infrastructure-playground/
├── app/
│   └── secret-vault/        # API Spring Boot
├── docker/
│   └── nginx/               # Configuracao do Nginx
├── scripts/                 # Scripts de automacao
├── docs/                    # Documentacao tecnica
├── backups/                 # Backups do banco
├── logs/                    # Logs da aplicacao
└── docker-compose.yml       # Orquestracao dos containers

## Documentacao

- [Arquitetura](docs/architecture.md)
- [Docker](docs/docker.md)
- [Linux e Scripts](docs/linux.md)

## Fases do projeto

| Fase | Status | Descricao |
|------|--------|-----------|
| 1    | ✅ | Infraestrutura base (Docker, Scripts, CI) |
| 2    | ✅ | API Spring Boot integrada |
| 3    | 🔜 | Testes automatizados |
| 4    | 🔜 | Deploy em VM na AWS |
| 5    | 🔜 | Pipeline CI/CD completo |
