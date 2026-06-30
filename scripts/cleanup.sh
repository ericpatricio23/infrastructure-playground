#!/bin/bash

set -e

BACKUP_DIR="./backups"
KEEP=5

echo "======================================"
echo " Infrastructure Playground - Cleanup"
echo "======================================"
echo ""

echo "Backups atuais:"
ls -lh "$BACKUP_DIR"
echo ""

TOTAL=$(find "$BACKUP_DIR" -maxdepth 1 -type f -name "*.sql.gz" | wc -l)

if [ "$TOTAL" -le "$KEEP" ]; then
  echo "[OK] Nada para remover. Total de backups: $TOTAL"
else
  REMOVE=$((TOTAL - KEEP))
  echo "Removendo $REMOVE backup(s) antigo(s)..."
  find "$BACKUP_DIR" -maxdepth 1 -type f -name "*.sql.gz" -printf '%T@ %p\n' \
    | sort -n \
    | head -n "$REMOVE" \
    | cut -d' ' -f2- \
    | xargs -r rm
  echo "[OK] Limpeza concluida. Backups mantidos: $KEEP"
fi

echo ""
echo "Backups apos limpeza:"
ls -lh "$BACKUP_DIR"

echo ""
echo "======================================"
echo " Cleanup concluido"
echo "======================================"
