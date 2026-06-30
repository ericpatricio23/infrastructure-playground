#!/bin/bash

set -e

BACKUP_DIR="./backups"
DATE=$(date +"%Y-%m-%d_%H-%M-%S")
FILENAME="postgres_backup_$DATE.sql.gz"

echo "======================================"
echo " Infrastructure Playground - Backup"
echo "======================================"
echo ""

STATUS=$(docker inspect --format='{{.State.Status}}' playground-postgres 2>/dev/null)
if [ "$STATUS" != "running" ]; then
  echo "[ERRO] Container playground-postgres nao esta rodando"
  exit 1
fi

echo "[OK] PostgreSQL rodando"
echo ""
echo "Gerando backup: $FILENAME"

docker exec playground-postgres pg_dump -U playground playgrounddb | gzip > "$BACKUP_DIR/$FILENAME"

echo "[OK] Backup salvo em: $BACKUP_DIR/$FILENAME"
echo ""
echo "Backups disponiveis:"
ls -lh "$BACKUP_DIR"

echo ""
echo "======================================"
echo " Backup concluido"
echo "======================================"
