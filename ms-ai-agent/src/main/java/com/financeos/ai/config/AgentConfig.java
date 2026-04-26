package com.financeos.ai.config;

import com.financeos.ai.agent.AssistenteFinanceiro;
import com.financeos.ai.tools.FinanceiroTools;
import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AgentConfig {

    @Value("${anthropic.api-key}")
    private String anthropicApiKey;

    @Value("${ms-finance.url}")
    private String msFinanceUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public FinanceiroTools financeiroTools(RestTemplate restTemplate) {
        return new FinanceiroTools(restTemplate, msFinanceUrl);
    }

    @Bean
    public AssistenteFinanceiro assistenteFinanceiro(FinanceiroTools tools) {
        AnthropicChatModel model = AnthropicChatModel.builder()
                .apiKey(anthropicApiKey)
                .modelName("claude-sonnet-4-20250514")
                .maxTokens(1000)
                .build();

        return AiServices.builder(AssistenteFinanceiro.class)
                .chatLanguageModel(model)
                .tools(tools)
                .systemMessageProvider(id ->
                    "Voce e um assistente financeiro pessoal inteligente. " +
                    "Responda sempre em portugues, de forma clara e objetiva. " +
                    "Use as ferramentas disponiveis para buscar dados reais. " +
                    "Formate valores como R$ X.XXX,XX.")
                .build();
    }
}
