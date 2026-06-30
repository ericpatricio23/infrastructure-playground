#!/bin/bash

set -e

echo "======================================"
echo " Infrastructure Playground - Deploy"
echo "======================================"
echo ""

# Verifica se o Docker esta rodando
if ! docker info > /dev/null 2>&1; then
  echo "[ERRO] Docker nao esta rodando"
  exit 1
fi

echo "[OK] Docker rodando"

# Gera backup antes do deploy
echo ""
echo "Gerando backup antes do deploy..."
bash scripts/backup.sh

# Para os containers
echo ""
echo "Parando containers..."
docker compose down

# Sobe os containers atualizados
echo ""
echo "Subindo containers..."
docker compose up -d

echo ""
echo "[OK] Deploy concluido"
echo ""
echo "Servicos disponiveis:"
echo "  Nginx    -> http://localhost"
echo "  Adminer  -> http://localhost:8080"
echo "  Postgres -> localhost:5432"
echo "  Redis    -> localhost:6379"

echo ""
echo "======================================"
echo " Deploy finalizado"
echo "======================================"
