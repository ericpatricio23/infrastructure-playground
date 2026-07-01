# Linux e Shell Scripts

## Visão Geral

O projeto utiliza Shell Scripts para automatizar tarefas repetitivas do ambiente.
Todos os scripts ficam na pasta `scripts/` e devem ser executados a partir da
raiz do projeto.

## Scripts

### setup.sh
Prepara o ambiente do zero. Deve ser o primeiro script executado após clonar
o repositório.

```bash
bash scripts/setup.sh
```

O que ele faz:
- Verifica se o Docker está rodando
- Verifica se o Docker Compose está disponível
- Cria as pastas necessárias (`logs/` e `backups/`)
- Sobe todos os containers com `docker compose up -d`

---

### healthcheck.sh
Verifica se todos os serviços estão saudáveis.

```bash
bash scripts/healthcheck.sh
```

O que ele faz:
- Verifica se o Docker está rodando
- Verifica o status de cada container individualmente
- Verifica se as portas 80, 8080, 5432 e 6379 estão abertas

---

### monitor.sh
Mostra o uso de recursos do sistema e dos containers.

```bash
bash scripts/monitor.sh
```

O que ele faz:
- Exibe uso de memória RAM com `free -h`
- Exibe uso de disco com `df -h`
- Exibe uso de CPU e memória de cada container com `docker stats`

---

### backup.sh
Gera um backup do banco de dados PostgreSQL.

```bash
bash scripts/backup.sh
```

O que ele faz:
- Verifica se o container do PostgreSQL está rodando
- Executa `pg_dump` dentro do container para exportar o banco
- Comprime o arquivo com `gzip`
- Salva em `backups/` com o nome `postgres_backup_YYYY-MM-DD_HH-MM-SS.sql.gz`

---

### cleanup.sh
Remove backups antigos, mantendo apenas os 5 mais recentes.

```bash
bash scripts/cleanup.sh
```

O que ele faz:
- Conta quantos backups existem na pasta `backups/`
- Se houver mais de 5, remove os mais antigos
- Usa `find` em vez de `ls` para manipulação segura de arquivos

---

### deploy.sh
Simula um deploy local do ambiente.

```bash
bash scripts/deploy.sh
```

O que ele faz:
- Verifica se o Docker está rodando
- Gera um backup antes de qualquer alteração
- Para todos os containers com `docker compose down`
- Sobe todos os containers novamente com `docker compose up -d`

---

## Comandos Linux Utilizados

| Comando | O que faz |
|---------|-----------|
| `chmod +x` | Dá permissão de execução a um arquivo |
| `mkdir -p` | Cria diretórios, ignorando erro se já existirem |
| `find` | Busca arquivos com filtros (tipo, nome, data) |
| `wc -l` | Conta linhas de um output |
| `sort -n` | Ordena numericamente |
| `head -n` | Pega os primeiros N itens |
| `tail -n` | Pega os últimos N itens |
| `xargs` | Passa output de um comando como argumento de outro |
| `gzip` | Comprime arquivos |
| `free -h` | Mostra uso de memória RAM |
| `df -h` | Mostra uso de disco |
| `ss -tln` | Lista portas abertas no sistema |

## Boas Práticas Adotadas

- `set -e` no início de cada script: interrompe a execução se qualquer comando falhar
- Verificação de pré-requisitos antes de executar ações (Docker rodando, container ativo)
- Mensagens claras de `[OK]` e `[ERRO]` para facilitar o diagnóstico
- Uso de `find` em vez de `ls` para manipulação segura de arquivos (recomendação ShellCheck SC2012)