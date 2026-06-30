#!/bin/bash

echo "======================================"
echo " Infrastructure Playground - Monitor"
echo "======================================"
echo ""

echo "--- Sistema ---"
echo "Uso de memória:"
free -h

echo ""
echo "Uso de disco:"
df -h /

echo ""
echo "--- Containers ---"
docker stats --no-stream --format "table {{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}"

echo ""
echo "======================================"
echo " Monitor concluído"
echo "======================================"
