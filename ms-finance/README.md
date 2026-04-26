# ms-finance

Microservico financeiro do FinanceOS. Gerencia transacoes, categorias e relatorios.

**Porta:** 8081

## Endpoints

| Metodo | Endpoint | Descricao |
|--------|----------|-----------|
| GET | /api/transacoes | Lista transacoes |
| GET | /api/transacoes/{id} | Busca por ID |
| POST | /api/transacoes | Cria transacao |
| PUT | /api/transacoes/{id} | Atualiza transacao |
| DELETE | /api/transacoes/{id} | Remove transacao |
| GET | /api/relatorios/mensal?mes=2025-04 | Relatorio mensal |
| GET | /api/relatorios/categorias?mes=2025-04 | Gastos por categoria |
| GET | /api/fluxo-caixa?mes=2025-04 | Fluxo de caixa |

## Rodar localmente

```bash
docker-compose up postgres
./mvnw spring-boot:run
```
