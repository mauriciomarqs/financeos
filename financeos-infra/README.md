# financeos-infra

Infraestrutura local do FinanceOS com Docker Compose.

## Subir tudo

```bash
# 1. Copie e configure o .env
cp .env.example .env
# Edite o .env com sua ANTHROPIC_API_KEY

# 2. Suba todos os serviços
docker-compose up -d

# 3. Verificar status
docker-compose ps
```

## Serviços

| Serviço | Porta | Descrição |
|---------|-------|-----------|
| postgres | 5432 | Banco de dados |
| ms-finance | 8081 | API financeira |
| ms-auth | 8082 | Autenticação |
| ms-ai-agent | 8083 | Agente IA |

## Só o banco (para dev local)

```bash
docker-compose up postgres -d
```
