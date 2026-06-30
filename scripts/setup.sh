#!/bin/bash

set -e

echo "======================================"
echo "  Infrastructure Playground - Setup"
echo "======================================"


if ! docker info > /dev/null 2>&1; then
  echo " Docker não está rodando. Inicie o Docker e tente novamente."
  exit 1
fi

echo " Docker está rodando"


if ! docker compose version > /dev/null 2>&1; then
  echo " Docker Compose não encontrado."
  exit 1
fi

echo " Docker Compose disponvel" 

mkdir -p logs backups

echo " Pastas criadas"

echo ""
echo "Subindo containers..."
docker compose up -d

echo ""
echo " Ambiente pronto!"
echo ""
echo "Serviços disponíveis:"
echo "  Nginx    → http://localhost"
echo "  Adminer  → http://localhost:8080"
echo "  Postgres → localhost:5432"
echo "  Redis    → localhost:6379" 
