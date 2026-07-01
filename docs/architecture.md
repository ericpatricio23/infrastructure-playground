# Arquitetura do Infrastructure Playground

## Visão Geral

O Infrastructure Playground é um ambiente completo de desenvolvimento que simula
a infraestrutura utilizada em aplicações reais. O objetivo é praticar e demonstrar
conhecimentos em Linux, Shell Script, Docker, Docker Compose e GitHub Actions,
com uma base preparada para receber uma API Spring Boot futuramente.

## Diagrama de Serviços
Cliente (Navegador)
│
▼
Nginx (porta 80)
│
▼
playground-network (rede bridge interna)
│
├── PostgreSQL (porta 5432)
├── Redis      (porta 6379)
└── Adminer    (porta 8080)

## Tecnologias Utilizadas

### Docker e Docker Compose
Escolhidos para isolar cada serviço em seu próprio container, garantindo que o
ambiente de desenvolvimento seja idêntico em qualquer máquina. O Docker Compose
orquestra todos os serviços com um único comando.

### Nginx
Atua como ponto de entrada da infraestrutura. Hoje serve uma resposta estática,
mas está preparado para funcionar como reverse proxy quando a API Spring Boot
for adicionada na Fase 2.

### PostgreSQL
Banco de dados relacional escolhido por ser robusto, open source e amplamente
utilizado em aplicações Java/Spring Boot. Os dados são persistidos em um volume
Docker, garantindo que não sejam perdidos ao reiniciar os containers.

### Redis
Solução de cache em memória. Será utilizado futuramente para armazenar sessões
e resultados de consultas frequentes, reduzindo a carga no banco de dados.

### Adminer
Interface web para administração do PostgreSQL. Facilita a visualização e
manipulação dos dados durante o desenvolvimento, sem precisar usar a linha
de comando.

## Rede Interna

Todos os serviços estão conectados na mesma rede bridge chamada
`playground-network`. Isso permite que eles se comuniquem entre si usando
o nome do serviço como hostname (por exemplo, o Adminer acessa o PostgreSQL
pelo endereço `postgres`, não por um IP fixo).

## Decisões de Design

- **Volumes nomeados** para o PostgreSQL garantem persistência dos dados
  independente do ciclo de vida dos containers.
- **Portas explícitas** mapeadas para o host facilitam o acesso durante
  o desenvolvimento.
- **Rede bridge customizada** em vez da rede padrão do Docker, para ter
  controle explícito sobre quais serviços se comunicam.

## Evolução Planejada

| Fase | O que será adicionado |
|------|-----------------------|
| 1    | Infraestrutura base (atual) |
| 2    | API Spring Boot integrada ao Nginx, PostgreSQL e Redis |
| 3    | Testes automatizados na pipeline CI |
| 4    | Deploy em VM Linux na AWS |
| 5    | Pipeline completo de CI/CD com deploy automatizado |