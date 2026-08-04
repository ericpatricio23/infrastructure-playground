#!/bin/bash

echo "======================================"
echo " Infrastructure Playground - Health Check"
echo "======================================"
echo ""

if docker info > /dev/null 2>&1; then
  echo "✅ Docker rodando"
else
  echo "❌ Docker não está rodando"
  exit 1
fi

CONTAINERS=("playground-postgres" "playground-redis" "playground-secret-vault" "playground-nginx")

for CONTAINER in "${CONTAINERS[@]}"; do
  STATUS=$(docker inspect --format='{{.State.Status}}' "$CONTAINER" 2>/dev/null)
  HEALTH=$(docker inspect --format='{{.State.Health.Status}}' "$CONTAINER" 2>/dev/null)
  if [ "$STATUS" = "running" ]; then
    if [ -n "$HEALTH" ] && [ "$HEALTH" != "<no value>" ]; then
      echo "✅ $CONTAINER → running ($HEALTH)"
    else
      echo "✅ $CONTAINER → running"
    fi
  else
    echo "❌ $CONTAINER → ${STATUS:-not found}"
  fi
done

echo ""
echo "Verificando portas expostas ao host..."
echo ""

# Postgres (5432) e Redis (6379) não devem aparecer aqui:
# ficam isolados na rede interna 'backend' de propósito.
PORTS=(80)
NAMES=("Nginx")

for i in "${!PORTS[@]}"; do
  PORT=${PORTS[$i]}
  NAME=${NAMES[$i]}
  if ss -tln | grep -q ":$PORT "; then
    echo "✅ $NAME → porta $PORT aberta"
  else
    echo "❌ $NAME → porta $PORT fechada"
  fi
done

# Adminer só existe no docker-compose.override.yml (uso local via Compose).
if docker inspect playground-adminer > /dev/null 2>&1; then
  if ss -tln | grep -q ":8080 "; then
    echo "✅ Adminer (dev local) → porta 8080 aberta"
  else
    echo "❌ Adminer (dev local) → porta 8080 fechada"
  fi
fi

echo ""
echo "======================================"
echo " Health check concluído"
echo "======================================"
