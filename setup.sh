#!/bin/bash
# FinanceOS — Script de setup completo
# Uso: bash setup.sh

set -e

REPO_URL="https://github.com/mauriciomarqs/financeos.git"
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  FinanceOS — Setup Completo            ${NC}"
echo -e "${BLUE}========================================${NC}"

echo -e "\n${GREEN}[1/4] Verificando dependências...${NC}"
command -v git >/dev/null 2>&1 || { echo "git não encontrado. Instale o git."; exit 1; }
command -v docker >/dev/null 2>&1 || { echo "docker não encontrado. Instale o Docker."; exit 1; }
echo "  git e docker encontrados!"

echo -e "\n${GREEN}[2/4] Configurando .env...${NC}"
if [ ! -f "financeos-infra/.env" ]; then
  cp financeos-infra/.env.example financeos-infra/.env
  echo "  Arquivo .env criado em financeos-infra/.env"
  echo "  ⚠️  EDITE o arquivo e coloque sua ANTHROPIC_API_KEY!"
else
  echo "  .env já existe, pulando..."
fi

echo -e "\n${GREEN}[3/4] Subindo banco de dados...${NC}"
cd financeos-infra
docker-compose up postgres -d
echo "  PostgreSQL subindo na porta 5432..."
cd ..

echo -e "\n${GREEN}[4/4] Pronto!${NC}"
echo ""
echo "  Próximos passos:"
echo "  1. Edite financeos-infra/.env com sua ANTHROPIC_API_KEY"
echo "  2. cd ms-finance && ./mvnw spring-boot:run"
echo "  3. Teste: curl http://localhost:8081/api/transacoes"
echo ""
echo -e "${BLUE}  Documentação: README.md${NC}"
echo -e "${BLUE}  Repositório: ${REPO_URL}${NC}"
