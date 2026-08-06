# Infrastructure Playground
Ambiente completo para desenvolver, automatizar e realizar deploy de aplicações utilizando Docker, Nginx e uma API Spring Boot segura.
---
## 📚 Visão Geral
```text
Cliente
   │
   ▼
Nginx (porta 80) ── rede "frontend"
   │
   ├── /auth/** ─────► Spring Boot API
   └── /api/** ──────► Spring Boot API
                           │
                    rede "backend" (isolada, sem acesso externo)
                           │
                           ├── PostgreSQL
                           └── Redis

Em desenvolvimento local (docker-compose.override.yml):
Adminer (porta 8080) ── acesso direto ao PostgreSQL, apenas na sua máquina
```
PostgreSQL, Redis e a API não ficam expostos diretamente à internet — só o Nginx recebe tráfego externo. Essa segmentação existe por padrão em `docker-compose.yml` (o que vai para produção); o acesso direto ao banco/cache só é liberado localmente, via `docker-compose.override.yml`.

---
## 🚀 Tecnologias
### Infraestrutura
- Docker
- Docker Compose (multi-arquivo: base + override para dev local)
- Nginx (Reverse Proxy)
- PostgreSQL 16
- Redis 7
- GitHub Actions (CI: build, testes automatizados, lint de scripts, validação de compose)
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
- Spring Boot Actuator (health checks)
- Tratamento global de exceções (respostas de erro padronizadas, sem vazamento de stack trace)
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

### Configure as variáveis de ambiente
O projeto usa um arquivo `.env` para credenciais e segredos, que **não é versionado no Git**. Copie o template e ajuste os valores:
```bash
cp .env.example .env
```
Edite o `.env` com valores próprios (senha do banco, segredo JWT, chave de criptografia). Para gerar um segredo JWT forte:
```bash
openssl rand -hex 32
```

### Inicie o ambiente
```bash
bash scripts/setup.sh
```
Todos os containers serão criados automaticamente. Em desenvolvimento local, o `docker-compose.override.yml` é aplicado por padrão junto com o `docker-compose.yml`, adicionando o Adminer e expondo as portas do PostgreSQL/Redis na sua máquina.

Para simular o ambiente de produção (sem Adminer, sem portas de banco expostas), rode:
```bash
docker compose -f docker-compose.yml up -d --build
```
---
## 🌐 Serviços

**Ambiente local (com override):**

| Serviço | Endereço |
|---------|----------|
| API (via Nginx) | http://localhost |
| Adminer | http://localhost:8080 |
| PostgreSQL | localhost:5432 |
| Redis | localhost:6379 |

**Produção (`docker-compose.yml` puro):** apenas a API via Nginx (`http://localhost` ou domínio configurado) fica acessível externamente. PostgreSQL e Redis só são alcançáveis pela rede interna do Docker.

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

### Monitoramento
| Método | Endpoint | Descrição |
|---------|----------|-----------|
| GET | `/actuator/health` | Health check da API (usado pelo Docker Compose) |

---
## 🧪 Testes
A API conta com testes unitários para as principais regras de negócio (autenticação, CRUD de segredos, criptografia e geração/validação de JWT), além do teste de contexto do Spring Boot.

```bash
cd app/secret-vault

# Sobe apenas os bancos, expostos localmente via override
docker compose up -d postgres redis

./mvnw test
```

O CI (GitHub Actions) roda a mesma suíte de testes automaticamente a cada push, usando containers de Postgres e Redis próprios do pipeline.

---
## 📜 Scripts
| Script | Descrição |
|---------|-----------|
| `bash scripts/setup.sh` | Prepara e sobe o ambiente |
| `bash scripts/healthcheck.sh` | Verifica a saúde dos serviços e portas expostas |
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
├── .env.example
├── docker-compose.yml           # Base — usado em produção
└── docker-compose.override.yml  # Extras de dev local (não versionado em produção)
```
---
## 🔒 Segurança
- Segredos (senha do banco, chave JWT, chave de criptografia) ficam em `.env`, fora do controle de versão
- PostgreSQL e Redis não são expostos publicamente por padrão — apenas o Nginx recebe tráfego externo
- Erros da API retornam respostas padronizadas, sem detalhes internos (stack traces) expostos ao cliente
- Cada serviço tem limite de memória e CPU definido, evitando que um container consuma todos os recursos do host

---
## 📖 Documentação
- Arquitetura (`docs/architecture.md`)
- Docker (`docs/docker.md`)
- Linux e Scripts (`docs/linux.md`)
