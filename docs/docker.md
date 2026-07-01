# Docker

## Visão Geral

O projeto utiliza Docker Compose para orquestrar todos os serviços. Com um único
comando é possível subir ou derrubar toda a infraestrutura.

## Comandos Principais

```bash
# Subir todos os containers em segundo plano
docker compose up -d

# Parar e remover todos os containers
docker compose down

# Ver status dos containers
docker compose ps

# Ver logs de um serviço específico
docker compose logs nginx
docker compose logs postgres

# Entrar dentro de um container
docker exec -it playground-postgres bash
```

## Serviços

### PostgreSQL

```yaml
postgres:
  image: postgres:16
  container_name: playground-postgres
  environment:
    POSTGRES_USER: playground
    POSTGRES_PASSWORD: playground123
    POSTGRES_DB: playgrounddb
  ports:
    - "5432:5432"
  volumes:
    - postgres_data:/var/lib/postgresql/data
```

- **Imagem:** postgres:16 (versão LTS estável)
- **Usuário/Senha/Banco:** definidos via variáveis de ambiente
- **Volume:** `postgres_data` garante persistência dos dados
- **Porta:** 5432 exposta para conexões externas (ex: DBeaver, Adminer)

### Redis

```yaml
redis:
  image: redis:7-alpine
  container_name: playground-redis
  ports:
    - "6379:6379"
```

- **Imagem:** redis:7-alpine (versão leve baseada em Alpine Linux)
- **Porta:** 6379 exposta para o host
- Não requer configuração adicional para uso em desenvolvimento

### Nginx

```yaml
nginx:
  image: nginx:alpine
  container_name: playground-nginx
  ports:
    - "80:80"
  volumes:
    - ./docker/nginx:/etc/nginx/conf.d
```

- **Imagem:** nginx:alpine
- **Volume:** a pasta `docker/nginx/` substitui a configuração padrão do Nginx
- **Porta:** 80 exposta para o host (acesso via http://localhost)
- O arquivo `docker/nginx/default.conf` define o comportamento do servidor

### Adminer

```yaml
adminer:
  image: adminer
  container_name: playground-adminer
  ports:
    - "8080:8080"
```

- Interface web para administração do PostgreSQL
- Acesso via http://localhost:8080
- Para conectar: sistema `PostgreSQL`, servidor `postgres`, usuário `playground`, senha `playground123`

## Volumes

```yaml
volumes:
  postgres_data:
```

Volume nomeado gerenciado pelo Docker. Os dados do PostgreSQL ficam salvos
mesmo que o container seja removido com `docker compose down`.

Para remover o volume e apagar todos os dados:

```bash
docker compose down -v
```

## Rede

```yaml
networks:
  playground-network:
    driver: bridge
```

Rede bridge customizada que conecta todos os serviços. Cada serviço é
acessível pelos demais usando seu nome como hostname. Por exemplo:
o Adminer acessa o PostgreSQL pelo endereço `postgres`.