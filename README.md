
# Infrastructure Playground

Ambiente completo para desenvolver, automatizar e fazer deploy de aplicações. Combina infraestrutura Docker com uma API Spring Boot segura.

## Visão Geral
Cliente
│
▼
Nginx (porta 80)
│
├── /auth/**  ──► Spring Boot API
├── /api/**   ──► Spring Boot API
│                     │
│                     ├── PostgreSQL
│                     └── Redis
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
- Java 21 + Spring Boot 3
- Spring Security + JWT
- Criptografia AES
- Spring Data JPA + PostgreSQL
- Spring Data Redis

## Como rodar

### Pré-requisitos
- Docker Desktop instalado e rodando
- Git

### Passos

```bash
git clone https://github.com/ericpatricio23/infrastructure-playground.git
cd infrastructure-playground
bash scripts/setup.sh
```

Aguarde todos os containers subirem. A API será buildada automaticamente.

## Serviços disponíveis

| Serviço    | URL                   |
|------------|-----------------------|
| API        | http://localhost      |
| Adminer    | http://localhost:8080 |
| PostgreSQL | localhost:5432        |
| Redis      | localhost:6379        |

## Endpoints

**Autenticação**
POST /auth/register   Cadastro de usuário
POST /auth/login      Login e geração de JWT

**Segredos** *(requer JWT no header Authorization: Bearer token)*
POST   /api/secrets        Criar segredo
GET    /api/secrets        Listar segredos
GET    /api/secrets/{id}   Buscar segredo por ID
PUT    /api/secrets/{id}   Atualizar segredo
DELETE /api/secrets/{id}   Remover segredo

## Scripts de automação

```bash
bash scripts/setup.sh        # Prepara e sobe o ambiente
bash scripts/healthcheck.sh  # Verifica saúde dos serviços
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
│   └── nginx/               # Configuração do Nginx
├── scripts/                 # Scripts de automação
├── docs/                    # Documentação técnica
├── backups/                 # Backups do banco
├── logs/                    # Logs da aplicação
└── docker-compose.yml       # Orquestração dos containers

## Documentação

- [Arquitetura](docs/architecture.md)
- [Docker](docs/docker.md)
- [Linux e Scripts](docs/linux.md)
