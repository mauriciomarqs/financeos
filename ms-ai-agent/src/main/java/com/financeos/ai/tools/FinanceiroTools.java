package com.financeos.ai.tools;

import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@RequiredArgsConstructor
public class FinanceiroTools {

    private final RestTemplate restTemplate;
    private final String msFinanceUrl;

    @Tool("Busca transacoes financeiras por mes no formato yyyy-MM.")
    public Object buscarTransacoesPorMes(String mes) {
        return restTemplate.getForObject(msFinanceUrl + "/api/relatorios/mensal?mes=" + mes, Object.class);
    }

    @Tool("Calcula o total de gastos por categoria em um mes no formato yyyy-MM.")
    public Map<?, ?> gastosPorCategoria(String mes) {
        return restTemplate.getForObject(msFinanceUrl + "/api/relatorios/categorias?mes=" + mes, Map.class);
    }

    @Tool("Retorna o fluxo de caixa (receitas, despesas e saldo) de um mes no formato yyyy-MM.")
    public Map<?, ?> fluxoCaixa(String mes) {
        return restTemplate.getForObject(msFinanceUrl + "/api/fluxo-caixa?mes=" + mes, Map.class);
    }
}
