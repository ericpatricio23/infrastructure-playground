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

TOTAL=$(ls "$BACKUP_DIR" | wc -l)

if [ "$TOTAL" -le "$KEEP" ]; then
  echo "[OK] Nada para remover. Total de backups: $TOTAL"
else
  REMOVE=$((TOTAL - KEEP))
  echo "Removendo $REMOVE backup(s) antigo(s)..."
  ls -t "$BACKUP_DIR" | tail -n "$REMOVE" | xargs -I {} rm "$BACKUP_DIR/{}"
  echo "[OK] Limpeza concluida. Backups mantidos: $KEEP"
fi

echo ""
echo "Backups apos limpeza:"
ls -lh "$BACKUP_DIR"

echo ""
echo "======================================"
echo " Cleanup concluido"
echo "======================================"
