package com.financeos.ai.service;

import com.financeos.ai.agent.AssistenteFinanceiro;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final AssistenteFinanceiro assistente;

    public String perguntar(String pergunta) {
        return assistente.perguntar(pergunta);
    }
}
