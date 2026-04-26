# ms-ai-agent

Agente de IA financeiro do FinanceOS. Usa LangChain4j + Claude API para responder perguntas em linguagem natural.

**Porta:** 8083

## O que o agente faz

- "Quanto gastei em alimentacao esse mes?"
- "Estou dentro do orcamento?"
- "Me da um resumo de marco."
- "Quando devo pagar minhas contas fixas?"

## Endpoints

| Metodo | Endpoint | Descricao |
|--------|----------|-----------|
| POST | /api/ai/perguntar | Envia uma pergunta ao agente |

## Configuracao

Defina a variavel de ambiente:

```bash
export ANTHROPIC_API_KEY=sk-ant-SuaChaveAqui
```

## Status

Fase 3 — em desenvolvimento.
