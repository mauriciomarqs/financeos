#!/bin/bash
# Script para enviar toda a estrutura ao GitHub
# Execute este script dentro da pasta financeos/

set -e

echo "========================================"
echo "  FinanceOS — Push para o GitHub"
echo "========================================"

# Verifica se já é um repositório git
if [ ! -d ".git" ]; then
  echo "[1/4] Inicializando repositório git..."
  git init
  git remote add origin https://github.com/mauriciomarqs/financeos.git
else
  echo "[1/4] Repositório git já inicializado."
fi

echo "[2/4] Adicionando todos os arquivos..."
git add .

echo "[3/4] Fazendo commit..."
git commit -m "feat: estrutura inicial do FinanceOS

- ms-finance: CRUD de transacoes, relatorios, fluxo de caixa
- ms-auth: estrutura de autenticacao JWT
- ms-ai-agent: agente IA com LangChain4j + Claude
- financeos-frontend: estrutura React + TypeScript
- financeos-backstage: catalog de servicos
- financeos-infra: Docker Compose + scripts"

echo "[4/4] Enviando para o GitHub..."
git branch -M main
git push -u origin main --force

echo ""
echo "Pronto! Acesse: https://github.com/mauriciomarqs/financeos"
