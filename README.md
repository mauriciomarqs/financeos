# 💰 FinanceOS

ERP Financeiro Modular com Microserviços Java, Backstage.io e Agente de IA.

## 🏗️ Arquitetura

```
financeos/
├── ms-finance/          # Microserviço financeiro (Spring Boot :8081)
├── ms-auth/             # Microserviço de autenticação (Spring Boot :8082)
├── ms-ai-agent/         # Agente de IA com LangChain4j (Spring Boot :8083)
├── financeos-frontend/  # Interface React + TypeScript (:3000)
├── financeos-backstage/ # Portal Backstage.io (:7007)
└── financeos-infra/     # Docker Compose + Scripts
```

## 🚀 Quick Start

```bash
# 1. Clone o repositório
git clone https://github.com/mauriciomarqs/financeos.git
cd financeos

# 2. Configure variáveis de ambiente
cp financeos-infra/.env.example financeos-infra/.env
# Edite o .env com sua ANTHROPIC_API_KEY

# 3. Suba a infraestrutura
cd financeos-infra
docker-compose up -d
```

## 📋 Roadmap

| Fase | Módulo | Status |
|------|--------|--------|
| 1 | ms-finance | 🔨 Em construção |
| 2 | ms-auth | ⏳ Próximo |
| 3 | ms-ai-agent | ⏳ Semana 3 |
| 4 | Frontend React | ⏳ Semana 4 |
| 5 | Backstage.io | ⏳ Fase 2 |
| 6 | ms-estoque | 🔮 Futuro |
| 7 | ms-ecommerce | 🔮 Futuro |

## 🛠️ Stack

- **Backend:** Java 17 + Spring Boot 3
- **Frontend:** React + TypeScript + Tailwind CSS
- **IA:** Claude API + LangChain4j
- **Portal:** Backstage.io
- **Banco:** PostgreSQL 16
- **Infra:** Docker + Docker Compose
